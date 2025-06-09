package me.sanao1006.mint.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRoborazziPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
                apply("com.google.devtools.ksp")
            }
            android {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                        all { test ->
                            test.systemProperties["robolectric.logging.enabled"] = "true"
                            test.jvmArgs("-noverify")
                            test.systemProperties["robolectric.graphicsMode"] = "NATIVE"
                            test.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                            test.maxParallelForks = Runtime.getRuntime().availableProcessors()
                            test.maxHeapSize = "4096m"
                        }
                    }
                }

            }
            dependencies {
                testImplementation(libs.library("junit"))
                testImplementation(libs.library("androidx.junit"))
                testImplementation(libs.library("robolectric"))
                testImplementation(libs.library("androidx.espresso.core"))
                testImplementation(libs.library("roborazzi"))
                testImplementation(libs.library("roborazziCompose"))
                testImplementation(libs.library("composablePreviewScanner"))
                testImplementation(libs.library("composablePreviewScannerJvm"))
                testImplementation(libs.library("roborazziPreviewScannerSupport"))
                testImplementation(libs.library("junit.vintage.engine"))
                testImplementation(libs.library("androidx.ui.test.junit4.android"))
                testImplementation(libs.library("webp.imageio"))
            }

        }
    }
}