package me.sanao1006.mint.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKtorPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.google.devtools.ksp")
            apply("de.jensklingenberg.ktorfit")
        }

        dependencies {
            implementation(libs.library("ktor.client.android"))
            implementation(libs.library("ktor.client.okhttp"))
            implementation(libs.library("ktor.client.core"))
            implementation(libs.library("ktor.client.content.negotiation"))
            implementation(libs.library("ktor.serialization.kotlinx.json"))
            implementation(libs.library("ktor.client.encoding"))
            implementation(libs.library("ktor.client.logging"))
            implementation(libs.library("ktorfit.lib"))
            implementation(libs.library("ktorfit.converters.response"))
            implementation(libs.library("ktorfit.converters.flow"))
            ksp(libs.library("ktorfitKsp"))
        }
    }
}