import me.sanao1006.mint.convention.implementation

plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.hilt")
    id("mint.convention.androidFeature")
    id("mint.convention.spotless")
}

android {
    namespace = "me.sanao1006.feature.home"

    dependencies {
        implementation(projects.core.network)
        implementation(projects.core.model)
        implementation(projects.core.dataStore)
        implementation(projects.core.data)
        implementation(projects.core.designsystem)
        implementation(projects.misskeyStreaming)
        implementation(projects.core.screens)
        implementation(projects.core.ui)
        implementation(projects.core.domain)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }
}
