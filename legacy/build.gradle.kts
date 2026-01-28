import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Module :library
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.builtin.kotlin)
    alias(libs.plugins.legacy.kapt)
    id("maven-publish")
}

base {
    archivesName = "colorpicker_legacy_${libs.versions.app.version.name.get()}"
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

android {
    namespace = "io.syslogic.colorpicker.legacy"
    buildToolsVersion = libs.versions.android.build.tools.get()
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
            // it breaks the data-binding, eg. when running ./gradlew :library:publishToMavenLocal
            enableAndroidTestCoverage = false
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }

    lint {
        lintConfig = file("../lint.xml")
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

val javadoc by tasks.registering(Javadoc::class) {

    onlyIf {
        project.file("build/intermediates/aar_main_jar").exists()
    }

    title = "Color Picker ${libs.versions.app.version.name.get()} API"
    source = android.sourceSets.getByName("main").java.getSourceFiles()
    configurations["implementation"].isCanBeResolved = true

    // classpath = files(File("${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"))
    // android.bootClasspath.forEach { classpath += fileTree(it) }
    // classpath += fileTree(project.file("build/tmp/aarsToJars/").absolutePath)
    // classpath += configurations.implementation.get() as FileCollection
    isFailOnError = false

    options.verbose()
    (options as StandardJavadocDocletOptions).links("https://docs.oracle.com/en/java/javase/17/docs/api/")
    // (options as StandardJavadocDocletOptions).linksOffline("https://developer.android.com/reference", "${android.sdkDirectory}/docs/reference")
    (options as StandardJavadocDocletOptions).linkSource(true)
    (options as StandardJavadocDocletOptions).author(true)

    destinationDir = project.file("build/outputs/javadoc")
    exclude("**/BuildConfig.java", "**/R.java", "**/*.kt")

    doFirst {

        // extract AAR files
        configurations["implementation"].files
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
    from(android.sourceSets.named("main").get().java.srcDirs)
    archiveClassifier.set("sources")
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(javadoc.get())
    from(javadoc.get().destinationDir)
    archiveClassifier.set("javadoc")
}

artifacts {
    archives(javadocJar.get())
    archives(sourcesJar.get())
}

group   = "io.syslogic"
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
                    name = "Color Picker"
                    description = "A simple color-picker library for Android"
                    url = "https://github.com/syslogic/androidx-colorpicker"
                    scm {
                        connection = "scm:git:git://github.com/syslogic/androidx-colorpicker.git"
                        developerConnection = "scm:git:ssh://github.com/syslogic/androidx-colorpicker.git"
                        url = "https://github.com/syslogic/androidx-colorpicker/"
                    }
                }
            }
        }
    }
}
