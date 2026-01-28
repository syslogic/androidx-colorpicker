// Root project build.gradle
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.builtin.kotlin) apply false
    alias(libs.plugins.legacy.kapt) apply false
}

tasks.register<Delete>("clean") {
    delete(
        rootProject.file("build"),
        project.file("build")
    )
}
