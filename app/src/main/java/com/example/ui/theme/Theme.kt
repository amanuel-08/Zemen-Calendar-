package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ZemenGold,
    onPrimary = Color(0xFF1E1700),
    primaryContainer = Color(0xFF3D3200),
    onPrimaryContainer = ZemenGoldLight,

    secondary = ZemenEmeraldLight,
    onSecondary = Color(0xFF003915),
    secondaryContainer = Color(0xFF0F5224),
    onSecondaryContainer = Color(0xFF8FF5A2),

    tertiary = ZemenAmber,
    onTertiary = Color(0xFF451F00),

    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = ZemenGoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF1C6),
    onPrimaryContainer = Color(0xFF534100),

    secondary = ZemenEmerald,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC7F0CA),
    onSecondaryContainer = Color(0xFF063B14),

    tertiary = ZemenAmber,
    onTertiary = Color.White,

    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightOutline
)

@Composable
fun ZemenTheme(
    themePreference: String = "system", // "system", "light", "dark"
    content: @Composable () -> Unit
) {
    val darkTheme = when (themePreference) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Keep backwards-compatible alias for existing templates/tests
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
