package me.sanao1006.mint.convention

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<BaseAppModuleExtension> {
                android {
                    compileSdkVersion(libs.version("androidCompileSdk").toInt())
                    defaultConfig {
                        applicationId = "me.sanao1006.mint"
                        minSdk = libs.version("androidMinSdk").toInt()
                        targetSdk = libs.version("androidTargetSdk").toInt()
                        versionCode = 1
                        versionName = "1.0"
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                    packagingOptions {
                        resources {
                            excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        }
                    }
                    buildTypes {
                        getByName("debug") {
                            isMinifyEnabled = false
                            applicationIdSuffix = ".debug"
                            buildConfigField("boolean", "DEBUG", "true")
                        }
                        getByName("release") {
                            applicationIdSuffix = ".release"
                            isMinifyEnabled = true
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                "proguard-rules.pro"
                            )
                        }
                    }
                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_21
                        targetCompatibility = JavaVersion.VERSION_21
                    }
                    testOptions {
                        unitTests {
                            isIncludeAndroidResources = true
                        }
                    }
                    buildFeatures {
                        buildConfig = true
                    }
                }
            }
        }
    }
}