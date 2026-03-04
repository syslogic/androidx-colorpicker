import com.android.build.api.dsl.FusedLibraryExtension
import com.android.build.api.dsl.MinSdkSpec

plugins {
    alias(libs.plugins.android.fusedlibrary)
    id("maven-publish")
}

configure<FusedLibraryExtension> {
    namespace = "io.syslogic.colorpicker"
    minSdk {
        release(version = Integer.parseInt(libs.versions.android.min.sdk.get())) as MinSdkSpec
    }
}

dependencies {
    include(project(path = ":legacy"))
    include(project(path = ":compose"))
}
