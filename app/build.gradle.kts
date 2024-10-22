plugins {
    alias(libs.plugins.kspPlugin)
    id("mint.convention.androidApplication")
    id("mint.convention.application.compose")
    alias(libs.plugins.kotlinGradlePlugin)
}

android {
    namespace = "me.sanao1006.mint"
    dependencies {
        api(libs.circuit.codegen.annotations)
        implementation(libs.circuit.foundation)
        implementation(libs.circuit.runtime)
        implementation(libs.circuit.overlay)
        implementation(libs.circuitx.overlay)
        implementation(libs.circuitx.gesture.navigation)
        implementation(libs.circuit.retained)
        implementation(libs.circuitx.effects)
        ksp(libs.circuit.codegen)
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
}

ksp {
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "hilt")
}