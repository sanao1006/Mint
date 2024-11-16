plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.hilt")
    id("mint.convention.androidFeature")
}

android {
    namespace = "me.sanao1006.feature.home"

    dependencies {
        implementation(projects.core.network)
        implementation(projects.core.model)
        implementation(projects.core.dataStore)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }
}
