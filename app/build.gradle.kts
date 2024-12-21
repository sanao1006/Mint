import me.sanao1006.mint.convention.implementation

plugins {
    alias(libs.plugins.kspPlugin)
    id("mint.convention.androidApplication")
    id("mint.convention.application.compose")
    id("mint.convention.ktorPlugin")
    id("mint.convention.hilt")
    id("mint.convention.spotless")
}

android {
    namespace = "me.sanao1006.mint"
    dependencies {
        implementation(libs.timber)
        api(libs.circuit.codegen.annotations)
        implementation(libs.bundles.circuit)
        ksp(libs.circuit.codegen)

        implementation(libs.bundles.ktor)
        ksp(libs.ktorfitKsp)

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.android)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation(projects.feature.login)
        implementation(projects.feature.home)
        implementation(projects.feature.user)
        implementation(projects.feature.search)
        implementation(projects.feature.notification)
        implementation(projects.core.network)
        implementation(projects.core.dataStore)
        implementation(projects.core.designsystem)
        implementation(projects.core.screens)
        implementation(projects.core.domain)
        implementation(projects.misskeyStreaming)
        implementation(libs.androidx.splashscreen)
    }
}

ksp {
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "hilt")
}