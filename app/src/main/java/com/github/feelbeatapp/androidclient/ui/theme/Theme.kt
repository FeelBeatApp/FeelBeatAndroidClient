package com.github.feelbeatapp.androidclient.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFF1DB954),
        onPrimary = Color.White,
        background = Color(0xFF222222),
        primaryContainer = Color(0xFF111111),
        surface = Color(0xFF222222),
        onSurface = Color.White,
        onSurfaceVariant = Color(0xFFDDDDDD),
        secondaryContainer = Color(0xFF111111),
        onSecondaryContainer = Color(0xFF1DB954),
        surfaceContainerHigh = Color(0xFF111111),
        surfaceContainerHighest = Color(0xFF111111),
        surfaceContainerLowest = Color(0xFF111111),
        surfaceContainerLow = Color(0xFF111111),
        onPrimaryContainer = Color(0xFFEEEEEE),
        errorContainer = Color(0xFF93000A),
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFF1DB954),
        primaryContainer = Color(0xFFD3F8E0),
        secondaryContainer = Color(0xFFE8E8E8),
    )

@Composable
fun FeelBeatTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme =
        when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
