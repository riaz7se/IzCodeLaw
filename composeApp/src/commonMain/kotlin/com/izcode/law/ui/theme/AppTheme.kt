package com.izcode.law.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Custom colors
val Purple = Color(0xFF6200EE)
val PurpleDark = Color(0xFF3700B3)
val Teal = Color(0xFF03DAC5)
val PurpleLight = Color(0xFFBB86FC)

// Light Theme colors
private val LightThemeColors = lightColors(
    primary = Purple,
    primaryVariant = PurpleDark,
    secondary = Teal,
    // Add more color customizations
)

// Dark Theme colors
private val DarkThemeColors = darkColors(
    primary = PurpleLight,
    primaryVariant = Purple,
    secondary = Teal,
    // Add more color customizations
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkTheme) DarkThemeColors else LightThemeColors,
        content = content
    )
} 