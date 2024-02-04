package com.example.gengo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val darkColorScheme = darkColorScheme(
    primary = Red,
    onPrimary = Black,
    primaryContainer = Red,
    onPrimaryContainer = Black,

    secondary = Blue,
    onSecondary = Black,
    secondaryContainer = Blue,
    onSecondaryContainer = Black,

    tertiary = Green,
    onTertiary = Black,
    tertiaryContainer = Green,
    onTertiaryContainer = Black,

    background = Black,
    onBackground = White,

    surface = Red,
    onSurface = Black,
    surfaceTint = Black,
    inverseSurface = White,
    inverseOnSurface = Black,

    error = Red,
    onError = Black,
    errorContainer = Red,
    onErrorContainer = Black,
)

private val lightColorScheme = lightColorScheme(
    primary = Red,
    onPrimary = Black,
    primaryContainer = Red,
    onPrimaryContainer = Black,

    secondary = Blue,
    onSecondary = Black,
    secondaryContainer = Blue,
    onSecondaryContainer = Black,

    tertiary = Green,
    onTertiary = Black,
    tertiaryContainer = Green,
    onTertiaryContainer = Black,

    background = White,
    onBackground = Black,

    surface = Red,
    onSurface = Black,
    surfaceTint = Black,
    inverseSurface = White,
    inverseOnSurface = Black,

    error = Red,
    onError = Black,
    errorContainer = Red,
    onErrorContainer = Black,
)

@Composable
fun GengoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    fontSizePrefs: FontSizePrefs = FontSizePrefs.DEFAULT,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getPersonalizedTypography(fontSizePrefs, if (darkTheme) White else Black),
        content = content
    )
}