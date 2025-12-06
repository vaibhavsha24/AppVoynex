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

//    val DarkColors = darkColorScheme(
//
//        // Primary Brand Glow (Electric Cyan)
//        primary = Color(0xFF00E5FF),     // Neon cyan – strong but elegant
//        onPrimary = Color.Black,         // Readable text on neon
//
//        // Background (Deep Sea)
//        background = Color(0xFF06141F),  // Very deep ocean navy
//        onBackground = Color(0xFFD0E8FF),// Soft moon-blue text
//
//        // Cards (Glassy Depth)
//        surface = Color(0xFF0D2538),     // Dark blue glass card
//        onSurface = Color(0xFFB7D4FF),   // Smooth pastel blue text
//
//        // Secondary Accent (Aurora Blue)
//        secondary = Color(0xFF2979FF),   // Deep saturated royal blue
//        onSecondary = Color.White,
//
//        // UI Surfaces, Chips, Bottom Sheets
//        surfaceVariant = Color(0xFF132D42), // Blue-black translucent
//        onSurfaceVariant = Color(0xFF90B9EE),
//
//        // Highlight / Action elements
//        tertiary = Color(0xFF00C6A1),    // Aqua-green neon pop
//        onTertiary = Color.Black
//    )

    val DarkAurora = darkColorScheme(
        primary = Color(0xFF00E5FF),  // Neon cyan
        secondary = Color(0xFF7C4DFF),// Purple neon secondary
        tertiary = Color(0xFF00FCA8), // Mint green glow
        background = Color(0xFF050814), // Deep cosmic black
        surface = Color(0xFF0E1A2A),    // Layered dark panels
        surfaceVariant = Color(0xFF16263A),
        onPrimary = Color.Black,
        onBackground = Color(0xFFE8F7FF),
        onSurface = Color(0xFFBBD8FF)
    )
    val PurpleRoyale = darkColorScheme(
        primary = Color(0xFFB388FF), // Lavender neon
        secondary = Color(0xFF7C4DFF),
        tertiary = Color(0xFFE040FB),
        background = Color(0xFF14081E), // Dark plum base
        surface = Color(0xFF1F1130),
        surfaceVariant = Color(0xFF2C1E41),
        onPrimary = Color.Black,
        onSurface = Color(0xFFE6CCFF)
    )
    val MidnightOcean = darkColorScheme(
        primary = Color(0xFF4DB6FF),
        secondary = Color(0xFF81D4FA),
        tertiary = Color(0xFF00B8D4),
        background = Color(0xFF071521),
        surface = Color(0xFF0E2433),
        surfaceVariant = Color(0xFF18394E),
        onPrimary = Color.Black,
        onSurface = Color(0xFFCDE8FF),
        onBackground = Color(0xFFB9DBFF)
    )

}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Theme.MidnightOcean,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}


