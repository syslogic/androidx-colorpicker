import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(uri("https://jitpack.io"))
        mavenLocal()
    }
}

rootProject.name = "ColorPicker"

include(":legacy", ":compose")

/* JitPack: exclude module. */
if (System.getenv("JITPACK") == null) {
    include(":mobile")
}
