package com.example.famlistapp.ui.theme

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

// Define DarkColorScheme using the placeholder Purple/Pink colors.
// These can be customized with app-specific brand colors.
// E.g., primary = FamListPrimaryDark, etc.
// For specific UI elements (like a bought item's checkbox), it's often better to apply
// the color directly to the Composable or via a custom Composable style,
// rather than overriding broad ColorScheme roles like 'primary' or 'secondary'
// unless that's the desired global effect.
private val DarkColorScheme = darkColorScheme(
    primary = Purple80, // Example: Use FamListPrimaryDark for a custom primary
    secondary = PurpleGrey80,
    tertiary = Pink80
    // error = ImportantRed // Example of using specific color for error states
)

// Define LightColorScheme similarly.
private val LightColorScheme = lightColorScheme(
    primary = Purple40, // Example: Use FamListPrimary for a custom primary
    secondary = PurpleGrey40,
    tertiary = Pink40
    // error = ImportantRed

    /* Other default colors to override for more detailed theming:
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White, // Text/icon color on top of primary color
    onSecondary = Color.White, // Text/icon color on top of secondary color
    onTertiary = Color.White, // Text/icon color on top of tertiary color
    onBackground = Color(0xFF1C1B1F), // Text/icon color on top of background
    onSurface = Color(0xFF1C1B1F), // Text/icon color on top of surface (cards, sheets, menus)
    */
    // The OnlineIndicatorGreen or CompletedGreen could be applied via custom modifiers or
    // by creating custom composables that use these colors, rather than putting them directly in the scheme,
    // unless they map to a specific role like `primaryContainer` or similar.
)

@Composable
fun FamListAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Set to false if you want to force your custom brand colors.
    dynamicColor: Boolean = false, // Changed to false to prefer app-defined colors over system
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Status bar color and icon brightness adjustment
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to match the app's background or surface color
            // This depends on whether your main screen background is MaterialTheme.colorScheme.background or .surface
            window.statusBarColor = colorScheme.background.toArgb() // Or colorScheme.surface.toArgb()
            // Set status bar icons to be light or dark based on the theme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Typography defined in Typography.kt
        // Shapes can also be customized here if needed
        // shapes = Shapes,
        content = content
    )
}
