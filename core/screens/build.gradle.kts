plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.circuit")
}

dependencies {
    implementation(projects.core.model)
}
