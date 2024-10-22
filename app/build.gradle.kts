plugins {
    alias(libs.plugins.kspPlugin)
    id("mint.convention.androidApplication")
    id("mint.convention.application.compose")
    alias(libs.plugins.kotlinGradlePlugin)
}

android {
    kotlinOptions {
        jvmTarget = "21"
    }
    namespace = "me.sanao1006.mint"
    dependencies {
        api(libs.circuit.codegen.annotations)
        implementation(libs.bundles.circuit)
        ksp(libs.circuit.codegen)

        implementation(libs.bundles.ktor)
        ksp(libs.ktorfitKsp)
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
}

ksp {
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "hilt")
}