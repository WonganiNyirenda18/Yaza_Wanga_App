package com.example.yaza_wanga.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppLightColorScheme = lightColorScheme(
    primary = primaryBlue,
    onPrimary = Color.White,
    primaryContainer = lightBlueBg,
    onPrimaryContainer = primaryBlue,
    background = containerBg,
    onBackground = textDark,
    surface = Color.White,
    onSurface = textDark,
    surfaceVariant = containerBg,
    onSurfaceVariant = textGray
)

@Composable
fun Yaza_WangaTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppLightColorScheme,
        typography = Typography,
        content = content
    )
}
