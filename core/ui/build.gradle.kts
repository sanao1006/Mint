plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.circuit")
    id("mint.convention.spotless")
    id("mint.convention.roborazzi")
}

dependencies {
    implementation(projects.core.screens)
    implementation(projects.core.model)
    implementation(projects.core.resValues)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
}