package com.example.famlistapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with.
// These are baseline Material Design 3 types.
// Consider defining custom fonts (e.g., Noto Sans SC for Chinese) and applying them here.
// For example:
// val NotoSansSC = FontFamily(
//    Font(R.font.noto_sans_sc_regular, FontWeight.Normal),
//    Font(R.font.noto_sans_sc_medium, FontWeight.Medium),
//    Font(R.font.noto_sans_sc_bold, FontWeight.Bold)
// )
// Then, fontFamily = NotoSansSC below.

// For Large Font Support / Accessibility:
// 1. Use `LocalDensity.current.fontScale` to get the user's system font size preference.
//    `val currentFontScale = LocalDensity.current.fontScale`
// 2. Apply this scale to your base font sizes: `fontSize = BASE_SIZE_SP * currentFontScale`.
//    This ensures your app respects system-wide accessibility settings for font size.
//    Example: `fontSize = 16.sp * currentFontScale` (but be cautious, direct multiplication can lead to overly large text).
//    A more nuanced approach is to define type ramps that scale appropriately or offer predefined scaled typographies.
//
// 3. Alternatively, define different Typography sets (e.g., NormalTypography, LargeTypography, ExtraLargeTypography)
//    and select one based on a user preference within the app or the system font scale.
//    `val typography = if (userPrefersLargeFont) LargeTypography else NormalTypography`
//    This provides more control over how each text style scales.
//
// 4. Ensure UI layouts can accommodate larger text without breaking (e.g., using scrollable containers,
//    `Modifier.weight()`, `FlowRow`, `FlowColumn`, and avoiding fixed heights for text containers).

val Typography = Typography(
    // Display styles (large, medium, small) - for short, important text
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default, // Replace with custom font if available
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline styles (large, medium, small) - for high-emphasis text
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // Or FontWeight.SemiBold for more emphasis
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title styles (large, medium, small) - for medium-emphasis text
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // Or FontWeight.Medium
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium, // M3 spec suggests Medium
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body styles (large, medium, small) - for longer-form text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp // Or 0.15.sp based on M3
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp // Or 0.1.sp based on M3
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Label styles (large, medium, small) - for call-to-actions, buttons, captions
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
