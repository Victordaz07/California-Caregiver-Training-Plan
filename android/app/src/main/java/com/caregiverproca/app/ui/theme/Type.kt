package com.caregiverproca.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Type scale copied from /docs/design/DESIGN.md ("typography" tokens).
 *
 * The design system calls for Manrope (prose) and JetBrains Mono (label/technical
 * accent). Both are Google Fonts. To use the real typefaces instead of the system
 * default:
 *   1. Download the .ttf files for Manrope and JetBrains Mono.
 *   2. Add them to app/src/main/res/font/.
 *   3. Build a FontFamily(Font(R.font.manrope_regular), Font(R.font.manrope_semibold, FontWeight.SemiBold), ...)
 *      and a FontFamily(Font(R.font.jetbrains_mono_medium), ...) below, then swap
 *      them into ManropeFamily / MonoFamily.
 */
private val ManropeFamily = FontFamily.Default
private val MonoFamily = FontFamily.Monospace

val CaregiverTypography = Typography(
    displayLarge = TextStyle( // display-lg-mobile
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.01).em,
    ),
    headlineLarge = TextStyle( // headline-lg
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.01).em,
    ),
    headlineMedium = TextStyle( // headline-md
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    headlineSmall = TextStyle( // headline-sm
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle( // body-lg
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle( // body-md
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle( // body-sm
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle( // label-lg (mono)
        fontFamily = MonoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.02.em,
    ),
    labelMedium = TextStyle( // label-md (mono)
        fontFamily = MonoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.04.em,
    ),
    labelSmall = TextStyle( // label-sm (mono)
        fontFamily = MonoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.06.em,
    ),
)
