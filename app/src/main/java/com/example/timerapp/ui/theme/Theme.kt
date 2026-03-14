package com.example.timerapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = Blue80,
    secondary = BlueGrey80,
    tertiary = Teal80
)

private val LightColors = lightColorScheme(
    primary = Blue40,
    secondary = BlueGrey40,
    tertiary = Teal40
)

@Composable
fun TimerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}