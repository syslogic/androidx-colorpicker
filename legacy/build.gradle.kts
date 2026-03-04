// Module :legacy
import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    // alias(libs.plugins.builtin.kotlin)
    id("maven-publish")
}

@Suppress("PropertyName") val GITHUB_DEV: String by project
@Suppress("PropertyName") val GITHUB_EMAIL: String by project
@Suppress("PropertyName") val GITHUB_HANDLE: String by project

base {
    archivesName = "colorpicker_legacy_${libs.versions.app.version.name.get()}"
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

configure<LibraryExtension> {
    namespace = "io.syslogic.colorpicker.legacy"
    compileSdk = Integer.parseInt(libs.versions.android.compile.sdk.get())

    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.android.min.sdk.get())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(rootProject.file("consumer.pro").absolutePath)
    }

    sourceSets {
        getByName("main") {
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
        compose = false
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
}

// Gradle 9.0 deprecation fix
val implCls: Configuration by configurations.creating {
    extendsFrom(configurations.getByName("implementation"))
    isCanBeResolved = true
}

val javadocs by tasks.registering(Javadoc::class) {

    title = "Color Picker ${libs.versions.app.version.name.get()} API"
    group = "documentation"
    setExcludes(listOf("**/BuildConfig.java", "**/R.java", "**/*.kt"))
    destinationDir = project.file("build/outputs/javadoc")
    isFailOnError = false

    val sdkComponents = androidComponents::sdkComponents.get()
    val bootClasspath: List<RegularFile> = sdkComponents.bootClasspath.get()
    val sdkDirectory: Directory? = sdkComponents.sdkDirectory.get()

    println("sdkDirectory: $sdkDirectory")
    bootClasspath.forEach { println("bootClasspath: ${it.asFile.name}") }

    val compileSdk = project.extensions.getByType<LibraryExtension>().compileSdk
    println("compileSdk: $compileSdk")

    // val sourceSets: NamedDomainObjectContainer<out AndroidSourceSet> = project.extensions.getByType<LibraryExtension>().sourceSets
    // sourceSets.forEach { println("sourceSet: ${it.name}") }

    // source = android.sourceSets["main"].java.getSourceFiles()
    source = fileTree(projectDir.absolutePath + "/src/main/java")
    source.files.forEach { println("source file: ${it.name}") }

    classpath = files(File("${sdkDirectory}/platforms/${compileSdk}/android.jar"))
    classpath += implCls as FileCollection
    bootClasspath.forEach { classpath += fileTree(it) }
    classpath += fileTree(project.file("build/tmp/aarsToJars/").absolutePath)

    options {
        this as StandardJavadocDocletOptions
        outputLevel = JavadocOutputLevel.VERBOSE
        links("https://docs.oracle.com/en/java/javase/17/docs/api")
        links("https://developer.android.com/reference")
        linkSource(false)
        author(true)
    }

    /*
    onlyIf {
        project.file("build/intermediates/aar_main_jar").exists()
    }
    */

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

val sourcesJar by tasks.registering(Jar::class) {
    from(projectDir.absolutePath + "/src/main/java")
    archiveClassifier.set("sources")
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(javadocs.get())
    from(javadocs.get().destinationDir)
    archiveClassifier.set("javadoc")
}

// Gradle 9.1 deprecation fix
configurations {
    @Suppress("UnstableApiUsage")
    consumable("jars") {
        outgoing.artifact(javadocJar)
        outgoing.artifact(sourcesJar)
    }
}

tasks.named("assemble") {
    dependsOn(javadocJar)
    dependsOn(sourcesJar)
}

group = "io.syslogic"
version = libs.versions.app.version.name.get()

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components.getByName("release"))
                groupId = group as String?
                artifactId = "colorpicker-legacy"
                version = libs.versions.app.version.name.get()
                pom {
                    name = "Color Picker Legacy"
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
