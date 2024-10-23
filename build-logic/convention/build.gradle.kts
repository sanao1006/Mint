import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "me.sanao1006.mint.buildlogic"

repositories {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
        }
    }
    mavenCentral()
    gradlePluginPortal()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "17"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    implementation(libs.bundles.plugins)
    implementation(libs.ksp)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "mint.convention.androidApplication"
            implementationClass = "me.sanao1006.mint.convention.AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "mint.convention.androidLibrary"
            implementationClass = "me.sanao1006.mint.convention.AndroidLibraryPlugin"
        }
        register("applicationCompose"){
            id = "mint.convention.application.compose"
            implementationClass = "me.sanao1006.mint.convention.AndroidApplicationComposeConventionPlugin"
        }
        register("compose"){
            id = "mint.convention.library.compose"
            implementationClass = "me.sanao1006.mint.convention.AndroidLibraryComposeConventionPlugin"
        }
        register("circuit"){
            id = "mint.convention.circuit"
            implementationClass = "me.sanao1006.mint.convention.AndroidCircuitPlugin"
        }
        register("ktorPlugin") {
            id = "mint.convention.ktorPlugin"
            implementationClass = "me.sanao1006.mint.convention.AndroidKtorPlugin"
        }
        register("hilt") {
            id = "mint.convention.hilt"
            implementationClass = "me.sanao1006.mint.convention.AndroidHiltPlugin"
        }
        register("androidFeature") {
            id = "mint.convention.androidFeature"
            implementationClass = "me.sanao1006.mint.convention.AndroidFeaturePlugin"
        }
    }
}
