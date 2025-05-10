package com.example.cryptotracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColorScheme = lightColorScheme(
    primary        = Color(0xFF1E88E5), // blue
    onPrimary      = Color.White,
    secondary      = Color(0xFF43A047), // green
    onSecondary    = Color.White,
    background     = Color(0xFFF0F0F0),
    onBackground   = Color.Black,
    surface        = Color.White,
    onSurface      = Color.Black,
    error          = Color(0xFFB00020),
    onError        = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary        = Color(0xFF90CAF9),
    onPrimary      = Color.Black,
    secondary      = Color(0xFFA5D6A7),
    onSecondary    = Color.Black,
    background     = Color(0xFF121212),
    onBackground   = Color.White,
    surface        = Color(0xFF121212),
    onSurface      = Color.White,
    error          = Color(0xFFCF6679),
    onError        = Color.Black
)

private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 22.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp
    )
)

@Composable
fun CryptoTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        shapes = Shapes(),
        content = content
    )
}