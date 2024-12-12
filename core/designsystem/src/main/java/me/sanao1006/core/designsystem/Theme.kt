package me.sanao1006.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB2DFDB), // Lighter Mint Green
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFE0F2F1),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF80CBC4),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = Color.Black,
    background = Color(0xFFE0F7FA),
    onBackground = Color.Black,
    surface = Color(0xFFE0F2F1),
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)

// Color(0xFF1DA1F2)
private val DarkColors = darkColorScheme(
    primary = Color(0xFF1D66AA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1A91DA),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF14171A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF657786),
    onSecondaryContainer = Color.White,
    background = Color(0xFF15202B),
    onBackground = Color.White,
    surface = Color(0xFF192734),
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

val LocalMintColors = staticCompositionLocalOf<ColorScheme> {
    error("No MintColors provided")
}

@Composable
fun MintTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(LocalMintColors provides colorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
