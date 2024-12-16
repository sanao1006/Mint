plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.ktorPlugin")
    id("mint.convention.hilt")
    id("mint.convention.circuit")
    id("mint.convention.spotless")
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.resValues)
    implementation(libs.kotlinx.datetime)
}
