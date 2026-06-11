package com.mysanjeevni.mysanjeevni.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF26A69A),
    secondary = androidx.compose.ui.graphics.Color(0xFF80CBC4),
    background = androidx.compose.ui.graphics.Color(0xFF121212),
    surface = androidx.compose.ui.graphics.Color(0xFF1E1E1E),
)

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF26A69A),
    secondary = androidx.compose.ui.graphics.Color(0xFF00695C),
    background = androidx.compose.ui.graphics.Color(0xFFF5F7FA),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
)

@Composable
fun MySanjeevniTheme(
    // KEY: darkTheme parameter hona chahiye
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}