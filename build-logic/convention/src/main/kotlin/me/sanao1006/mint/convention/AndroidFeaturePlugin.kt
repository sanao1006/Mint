package me.sanao1006.mint.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("mint.convention.ktorPlugin")
                apply("mint.convention.circuit")
            }

            dependencies {

                implementation(libs.library("kotlinx.coroutines.core"))
                implementation(libs.library("kotlinx.coroutines.android"))
            }
        }
    }
}