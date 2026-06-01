package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val StudioColorScheme = darkColorScheme(
    primary = StudioPrimary,
    onPrimary = TextPrimary,
    secondary = StudioSecondary,
    onSecondary = StudioBackground,
    tertiary = StudioAccent,
    background = StudioBackground,
    onBackground = TextPrimary,
    surface = StudioCard,
    onSurface = TextPrimary,
    surfaceVariant = StudioCardLight,
    onSurfaceVariant = TextPrimary,
    error = StudioError,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StudioColorScheme,
        typography = Typography,
        content = content
    )
}
