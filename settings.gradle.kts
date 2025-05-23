import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com\\.(android|google).*")
                excludeGroupByRegex("org\\.jetbrains.*")
            }
        }
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "ColorPicker"

include(":library")

/* JitPack: exclude module. */
if (System.getenv("JITPACK") == null) {
    include(":mobile")
}
