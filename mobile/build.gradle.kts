import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Module :mobile
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

base {
    archivesName = "colorpicker_demo_${libs.versions.app.version.name.get()}"
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

android {
    namespace = "io.syslogic.demo.colorpicker"
    buildToolsVersion = libs.versions.android.build.tools.get()
    compileSdk = Integer.parseInt(libs.versions.android.compile.sdk.get())

    defaultConfig {
        applicationId = "io.syslogic.demo.colorpicker"
        minSdk = Integer.parseInt(libs.versions.android.min.sdk.get())
        targetSdk = Integer.parseInt(libs.versions.android.target.sdk.get())
        versionCode = Integer.parseInt(libs.versions.app.version.code.get())
        versionName = libs.versions.app.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testBuildType = "debug"
    }

    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        // stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
        // enableStrongSkippingMode = true
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
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }

    sourceSets {
        getByName("main") {
            java {
                srcDir("src/main/java")
            }
        }
    }

    packaging {
        jniLibs.keepDebugSymbols.add("**/libandroidx.graphics.path.so")
    }

    lint {
        lintConfig = file("../lint.xml")
        checkAllWarnings = true
        warningsAsErrors = true
        abortOnError = false
        showAll = false
    }
}

dependencies {

    // Use either Jitpack repository or project module :library (local).
    // implementation "io.syslogic:androidx-colorpicker:$version_name"
    implementation(project(path = ":library"))

    implementation(libs.material.design)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.appcompat)

    // androidTestImplementation(libs.androidx.navigation.testing)
    // implementation(libs.androidx.navigation.fragment.ktx)
    // implementation(libs.androidx.navigation.runtime.ktx)
    // implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.bundles.androidx.navigation3)

    // https://mvnrepository.com/artifact/androidx.compose
    // https://developer.android.com/jetpack/compose/tooling#bom
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.app)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // jUnit
    testImplementation(libs.junit)

    // Required for connected tests.
    // https://mvnrepository.com/artifact/androidx.test/monitor
    debugImplementation(libs.androidx.test.monitor)

    /* Composable Preview */
    debugImplementation(libs.androidx.compose.ui.tooling)

    // https://mvnrepository.com/artifact/androidx.test.ext
    androidTestImplementation(libs.bundles.androidx.test)

    // https://mvnrepository.com/artifact/androidx.test.espresso
    androidTestImplementation(libs.bundles.androidx.espresso)
    // The following dependency can be either "implementation" or "androidTestImplementation",
    // depending on whether you want it to appear on your APK's compile classpath or the test APK classpath.
    // androidTestImplementation libs.androidx.espresso.idling.resource

    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.compose.uitest)

    // Needed for createComposeRule, but not createAndroidComposeRule:
    debugImplementation(libs.androidx.compose.uitest.manifest)
}

// disable caching
configurations.configureEach {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}
