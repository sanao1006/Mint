plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.hilt")
    id("mint.convention.androidFeature")
    id("mint.convention.spotless")
}

android {
    dependencies {
        implementation(projects.core.network)
        implementation(projects.core.model)
        implementation(projects.core.dataStore)
        implementation(projects.core.data)
        implementation(projects.core.screens)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }
}