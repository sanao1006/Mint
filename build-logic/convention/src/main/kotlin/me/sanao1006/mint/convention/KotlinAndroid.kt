package me.sanao1006.mint.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKotlinAndroid(
    extension: LibraryExtension
) = extension.apply {
    compileSdk = libs.version("androidCompileSdk").toInt()

    val moduleName = path.split(":").drop(2).joinToString(".")
    namespace =
        if (moduleName.isNotEmpty()) "me.sanao1006.mint.$moduleName" else "me.sanao1006.mint"

    defaultConfig {
        minSdk = libs.version("androidMinSdk").toInt()
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        implementation(libs.library("timber"))
        implementation(libs.library("arrow.core"))
        implementation(libs.library("arrow.fx.coroutines"))
    }
}