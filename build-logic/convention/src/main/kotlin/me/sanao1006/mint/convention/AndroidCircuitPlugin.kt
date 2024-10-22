package me.sanao1006.mint.convention

import com.android.build.api.dsl.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AndroidCircuitPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.google.devtools.ksp")
        }

        this.extensions.configure<LibraryExtension> {
            dependencies {
                add("api", libs.library("circuit.codegen.annotations"))
                implementation(libs.library("circuit.foundation"))
                implementation(libs.library("circuit.runtime"))
                implementation(libs.library("circuit.overlay"))
                implementation(libs.library("circuitx.overlay"))
                implementation(libs.library("circuitx.gesture.navigation"))
                implementation(libs.library("circuit.retained"))
                implementation(libs.library("circuitx.effects"))
            }
        }
        extensions.configure<KspExtension> {
            arg("circuit.codegen.lenient", "true")
            arg("circuit.codegen.mode", "hilt")
        }
    }
}