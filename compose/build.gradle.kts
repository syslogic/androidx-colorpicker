// Module :compose
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.android)
    // alias(libs.plugins.builtin.kotlin)
    // alias(libs.plugins.legacy.kapt)
    alias(libs.plugins.dokka.html)
    alias(libs.plugins.dokka.javadoc)
    id("maven-publish")
}

@Suppress("PropertyName") val GITHUB_DEV: String by project
@Suppress("PropertyName") val GITHUB_EMAIL: String by project
@Suppress("PropertyName") val GITHUB_HANDLE: String by project

base {
    archivesName = "colorpicker_compose_${libs.versions.app.version.name.get()}"
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

configure<LibraryExtension> {
    namespace = "io.syslogic.colorpicker.compose"
    compileSdk = Integer.parseInt(libs.versions.android.compile.sdk.get())

    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.android.min.sdk.get())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(rootProject.file("consumer.pro").absolutePath)
    }

    @Suppress("UnstableApiUsage")
    sourceSets {
        named("main") {
            java.directories.add("src/main/java")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        compose = true
    }

    buildTypes {
        debug {
            // it breaks the data-binding, e.g. when running ./gradlew :library:publishToMavenLocal
            enableAndroidTestCoverage = false
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }

    lint {
        lintConfig = rootProject.file("lint.xml")
        checkAllWarnings = true
        warningsAsErrors = true
        abortOnError = false
        showAll = false

        // TODO: Cannot invoke "org.jetbrains.uast.kotlin.KotlinUastResolveProviderService
        //  .getBindingContext(org.jetbrains.kotlin.psi.KtElement)" because "service" is null.
        disable += "MutableCollectionMutableState"
        disable += "AutoboxingStateCreation"
    }

    packaging {
        resources.pickFirsts.add("META-INF/AL2.0")
        resources.pickFirsts.add("META-INF/LGPL2.1")
        jniLibs.keepDebugSymbols.add("**/libandroidx.graphics.path.so")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    implementation(libs.kotlin.stdlib)
    implementation(libs.material.design)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.preference.ktx)

    // https://mvnrepository.com/artifact/androidx.compose
    // https://developer.android.com/jetpack/compose/tooling#bom
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.lib)

    /* Composable Preview */
    debugImplementation(libs.bundles.androidx.compose.tooling)

    /* Dokka Android Documentation Plugin */
    dokkaPlugin(libs.dokka.android.documentation.plugin)
}

// Gradle 9.0 deprecation fix
val implCls: Configuration by configurations.creating {
    extendsFrom(configurations.getByName("implementation"))
    isCanBeResolved = true
}

// Dokka generation
dokka {

    dokkaSourceSets.named("main") {

        val sdkComponents = androidComponents::sdkComponents.get()
        val sdkDirectory: Directory? = sdkComponents.sdkDirectory.get()
        val compileSdk = project.extensions.getByType<LibraryExtension>().compileSdk

        sourceRoots.from(
            files(File("${sdkDirectory}/platforms/${compileSdk}/android.jar")),
            fileTree(project.file("build/tmp/aarsToJars").absolutePath),
            "${projectDir.absolutePath}/src/main/java"
        )

        sourceLink {
            localDirectory.set(file("${projectDir.absolutePath}/src/main/java"))
            remoteUrl("https://github.com/syslogic/androidx-colorpicker/tree/master/compose/src/main/java")
            remoteLineSuffix.set("#L")
        }
    }

    dokkaSourceSets.configureEach {

        enableJdkDocumentationLink.set(true)
        enableKotlinStdLibDocumentationLink.set(true)
        enableAndroidDocumentationLink.set(true)
        jdkVersion.set(17)

        dokkaPublications.javadoc {
            moduleName.set(project.name)
            moduleVersion.set(project.version.toString())
            outputDirectory.set(layout.buildDirectory.dir("dokka/javadoc"))
        }
        dokkaPublications.html {
            moduleName.set(project.name)
            moduleVersion.set(project.version.toString())
            outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        }
    }
}

val extractAar by tasks.registering(DefaultTask::class) {
    doFirst {
        // extract AAR files
        implCls.files
            .filter { it.name.endsWith(".aar") }
            .forEach { aar: File ->
                copy {
                    from(zipTree(aar))
                    include("**/classes.jar")
                    into(project.file("build/tmp/aarsToJars/${aar.name.replace(".aar", "")}/"))
                }
            }

        // provide JAR, which contains the generated data-binding classes
        val aarMain = project.file("build/intermediates/aar_main_jar")
        if (aarMain.exists()) {
            copy {
                from(aarMain)
                include("**/classes.jar")
                into(project.file("build/tmp/aarsToJars/aar_main_jar/"))
            }
        }
    }
}

val dokkaGenerateJavadocJar by tasks.registering(Jar::class) {
    group = "dokka"
    dependsOn(tasks.dokkaGeneratePublicationJavadoc)
    from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
    description = "Assembles a JAR containing the Javadoc-style documentation generated by Dokka."
}

val dokkaGenerateHtmlJar by tasks.registering(Jar::class) {
    group = "dokka"
    dependsOn(tasks.dokkaGeneratePublicationHtml)
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
    description = "Assembles a JAR containing the HTML documentation generated by Dokka."
}

val dokkaCleanJavadoc by tasks.registering(Delete::class) {
    group = "dokka"
    delete = setOf(project.file("build/dokka/javadoc"))
    description = "It removes the documentation generated by Dokka."
}

val dokkaCleanHtml by tasks.registering(Delete::class) {
    group = "dokka"
    delete = setOf(project.file("build/dokka/html"))
    description = "It removes the documentation generated by Dokka."
}

tasks.dokkaGeneratePublicationJavadoc.dependsOn(dokkaCleanJavadoc)
tasks.dokkaGeneratePublicationHtml.dependsOn(dokkaCleanHtml)
tasks.dokkaGenerate.dependsOn(extractAar)

val dokkaClean by tasks.registering {
    group = "dokka"
    dependsOn(dokkaCleanJavadoc, dokkaCleanHtml)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(project.file("src/main/java"))
}

// Gradle 9.1 deprecation fix
configurations {
    @Suppress("UnstableApiUsage")
    consumable("jars") {
        outgoing.artifact(dokkaGenerateJavadocJar)
        outgoing.artifact(sourcesJar)
    }
}

tasks.named("assemble") {
    dependsOn(dokkaGenerateJavadocJar)
    dependsOn(sourcesJar)
}

group   = "io.syslogic"
version = libs.versions.app.version.name.get()

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components.getByName("release"))
                groupId = group as String?
                artifactId = "colorpicker-compose"
                version = libs.versions.app.version.name.get()
                pom {
                    name = "Color Picker Compose"
                    description = "A simple color-picker library for Android"
                    url = "https://github.com/syslogic/androidx-colorpicker"
                    scm {
                        connection = "scm:git:git://github.com/syslogic/androidx-colorpicker.git"
                        developerConnection = "scm:git:ssh://github.com/syslogic/androidx-colorpicker.git"
                        url = "https://github.com/syslogic/androidx-colorpicker/"
                    }
                    developers {
                        developer {
                            name = GITHUB_DEV
                            email = GITHUB_EMAIL
                            id = GITHUB_HANDLE
                        }
                    }
                    licenses {
                        license {
                            name = "MIT License"
                            url = "http://www.opensource.org/licenses/mit-license.php"
                        }
                    }
                }
            }
        }
    }
}
