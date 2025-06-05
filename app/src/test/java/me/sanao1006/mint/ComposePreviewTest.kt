package me.sanao1006.mint

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.android.screenshotid.AndroidPreviewScreenshotIdBuilder
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview

@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ComposePreviewTest(
    private val preview: ComposablePreview<AndroidPreviewInfo>
) {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Config(application = Application::class)
    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Test
    fun snapshot() {
        val fileName = AndroidPreviewScreenshotIdBuilder(preview).ignoreClassName().build()
        val filePath =
            "${DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH}/$fileName.png" // Preview function name.png

        composeTestRule.apply {
            setContent { preview() }
            onRoot().captureRoboImage(filePath = filePath)
        }
    }

    companion object {
        private val cachedPreviews: List<ComposablePreview<AndroidPreviewInfo>> by lazy {
            AndroidComposablePreviewScanner()
                .scanPackageTrees(
                    include = listOf("**"), // Set up a package to look for Preview functions
                    exclude = listOf()
                )
                .includePrivatePreviews() // Include private Preview functions
                .getPreviews()
        }

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun values(): List<ComposablePreview<AndroidPreviewInfo>> = cachedPreviews
    }
}