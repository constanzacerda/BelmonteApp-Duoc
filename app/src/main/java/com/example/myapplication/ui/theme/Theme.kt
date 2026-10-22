package com.example.myapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BelmonteLightColorScheme = lightColorScheme(
    primary = AzulMarinoPrimary,
    onPrimary = Color.White,
    primaryContainer = AzulMarinoVariant,
    onPrimaryContainer = Color.White,
    secondary = DoradoPrimary,
    onSecondary = TextoSobreDorado,
    secondaryContainer = DoradoLight,
    onSecondaryContainer = TextoPrimarioLight,
    tertiary = NaranjoPrimary,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextoPrimarioLight,
    surface = SurfaceLight,
    onSurface = TextoPrimarioLight,
    surfaceVariant = Color(0xFFE9ECEF),
    onSurfaceVariant = TextoSecundarioLight,
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

private val BelmonteDarkColorScheme = darkColorScheme(
    primary = DoradoPrimary,
    onPrimary = AzulMarinoPrimary,
    primaryContainer = AzulMarinoLight,
    onPrimaryContainer = DoradoLight,
    secondary = NaranjoPrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4D2300),
    onSecondaryContainer = NaranjoLight,
    tertiary = DoradoDark,
    onTertiary = Color.White,
    background = BackgroundDark,
    onBackground = TextoPrimarioDark,
    surface = SurfaceDark,
    onSurface = TextoPrimarioDark,
    surfaceVariant = AzulMarinoLight,
    onSurfaceVariant = TextoSecundarioDark,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) BelmonteDarkColorScheme else BelmonteLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) BackgroundDark.toArgb() else AzulMarinoPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}