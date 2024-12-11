plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.circuit")
    id("mint.convention.spotless")
}

dependencies {
    implementation(projects.core.screens)
    implementation(projects.core.model)
    implementation(projects.core.resValues)
}