import com.android.build.api.dsl.FusedLibraryExtension
import com.android.build.api.dsl.MinSdkSpec

// this needs AGP 9.0, which Dokka doesn't support yet.
plugins {
    alias(libs.plugins.android.fusedlibrary)
    id("maven-publish")
}

@Suppress("PropertyName") val GITHUB_DEV: String by project
@Suppress("PropertyName") val GITHUB_EMAIL: String by project
@Suppress("PropertyName") val GITHUB_HANDLE: String by project

configure<FusedLibraryExtension> {
    namespace = "io.syslogic.colorpicker"
    // aarMetadata { minCompileSdk = 23 minCompileSdkExtension = 1 }
    minSdk {
        release(version = Integer.parseInt(libs.versions.android.min.sdk.get())) as MinSdkSpec
    }
}

dependencies {
    include(project(path = ":legacy"))
    include(project(path = ":compose"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            from(components["fusedLibraryComponent"])
            groupId = "io.syslogic"
            artifactId = "androidx-colorpicker"
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