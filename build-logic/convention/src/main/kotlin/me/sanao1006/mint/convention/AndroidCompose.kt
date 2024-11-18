package me.sanao1006.mint.convention

import com.android.build.api.dsl.CommonExtension

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    with(pluginManager) {
        apply(libs.findPlugin("jetbrainsCompose").get().get().pluginId)
        apply(libs.findPlugin("compose.compiler").get().get().pluginId)
    }
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        enableStrongSkippingMode.convention(true)
        val androidxCompose = "androidx.compose."
        dependencies {
            implementation(libs.library("${androidxCompose}runtime"))
            implementation(libs.library("${androidxCompose}foundation"))
            implementation(libs.library("${androidxCompose}foundation.layout"))
            implementation(libs.library("${androidxCompose}material3"))
            implementation(libs.library("${androidxCompose}material.iconsExtended"))
            implementation(libs.library("${androidxCompose}ui"))
            implementation(libs.library("${androidxCompose}ui.graphics"))
            implementation(libs.library("${androidxCompose}ui.tooling.preview"))
            implementation(libs.library("${androidxCompose}constraintlayout"))
            implementation(libs.library("androidx.activity.compose"))
            implementation(libs.library("androidx.navigation.compose"))
            implementation(libs.library("androidx.core.ktx"))
            implementation(libs.library("androidx.lifecycle.runtimeCompose"))
            implementation(libs.library("androidx.lifecycle.viewModelCompose"))
            implementation(libs.library("androidx.lifecycle.runtime.ktx"))
            implementation(libs.library("compose.icons"))

            debugImplementation(libs.library("${androidxCompose}ui.tooling"))
            debugImplementation(libs.library("${androidxCompose}ui.test.manifest"))
        }
    }
}