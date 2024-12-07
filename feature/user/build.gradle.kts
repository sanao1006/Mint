plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.hilt")
    id("mint.convention.androidFeature")
    id("mint.convention.spotless")
}

dependencies {
    implementation(projects.core.screens)
    implementation(projects.core.ui)
    implementation(projects.core.data)
}