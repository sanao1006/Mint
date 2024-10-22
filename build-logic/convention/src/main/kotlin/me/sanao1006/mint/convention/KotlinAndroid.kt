package me.sanao1006.mint.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    extension: LibraryExtension
) = extension.apply {
    compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()

    val moduleName = path.split(":").drop(2).joinToString(".")
    namespace = if(moduleName.isNotEmpty()) "me.sanao1006.mint.$moduleName" else "me.sanao1006.mint"

    defaultConfig {
        minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
        consumerProguardFiles("consumer-proguard-rules.pro")
        testOptions {
            targetSdk = libs.findVersion("android.targetSdk").get().requiredVersion.toInt()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}