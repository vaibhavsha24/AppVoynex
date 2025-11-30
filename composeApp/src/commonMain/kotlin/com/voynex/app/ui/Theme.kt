package com.voynex.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


object Theme {


    val LightColors = lightColorScheme(
        primary = Color(0xFF1565C0),          // Strong Blue (buttons, highlights)
        onPrimary = Color.White,

        background = Color(0xFFF5F9FF),       // Cloud Blue (new background)
        onBackground = Color(0xFF0D1B2A),     // Deep Navy text

        surface = Color(0xFFFFFFFF),          // White cards on blue background
        onSurface = Color(0xFF1B263B),

        secondary = Color(0xFF64B5F6),        // Soft Sky Blue
        onSecondary = Color.White,

        surfaceVariant = Color(0xFFE3F2FD),   // Very light sky blue (chips / fields)
        onSurfaceVariant = Color(0xFF455A64),

        tertiary = Color(0xFF0288D1),         // Bright cyan accents
        onTertiary = Color.White
    )


}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Theme.LightColors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}


