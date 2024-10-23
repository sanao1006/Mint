plugins {
    alias(libs.plugins.kspPlugin)
    id("mint.convention.androidApplication")
    id("mint.convention.application.compose")
    alias(libs.plugins.kotlinGradlePlugin)
    alias(libs.plugins.ktorfitPlugin)
    id("mint.convention.hilt")
}

android {
    namespace = "me.sanao1006.mint"
    dependencies {
        api(libs.circuit.codegen.annotations)
        implementation(libs.bundles.circuit)
        ksp(libs.circuit.codegen)

        implementation(libs.bundles.ktor)
        ksp(libs.ktorfitKsp)
    }
}

ksp {
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "hilt")
}