package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MexcColorScheme = darkColorScheme(
    primary = MexcPrimary,
    onPrimary = MexcBackground,
    secondary = MexcSecondary,
    onSecondary = MexcTextPrimary,
    tertiary = MexcColdBlue,
    background = MexcBackground,
    onBackground = MexcTextPrimary,
    surface = MexcSurface,
    onSurface = MexcTextPrimary,
    surfaceVariant = MexcSurfaceVariant,
    onSurfaceVariant = MexcTextSecondary,
    outline = MexcBorder
)

@Composable
fun MexcTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MexcColorScheme,
        typography = Typography,
        content = content
    )
}

