plugins {
    id("mint.convention.androidLibrary")
    id("mint.convention.library.compose")
    id("mint.convention.circuit")
    id("mint.convention.spotless")
    id("mint.convention.hilt")
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.resValues)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.dataStore)
}
