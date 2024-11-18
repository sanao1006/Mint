plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.ktorPlugin")
    id("mint.convention.hilt")
    id("mint.convention.circuit")
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.model)
}
