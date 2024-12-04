plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.ktorPlugin")
    id("mint.convention.hilt")
    id("mint.convention.spotless")
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.dataStore)
}