package com.discordbotmaker.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Grid Branding Identity — Visual direction of 2026-10-12 ─────────────────
//
// Brand:    Grid (by Shin5hi)
// Logo:     Minimalist geometric "G" — intersecting grid lines within a
//           rounded square. Rendered as vector/SVG for all density buckets.
// Primary:  Violet (#7C4DFF) — brand color for all interactive
//           elements, active states, and CTA buttons.
// Surface:  Deep Charcoal navy (#0B0B14 base, #151522 cards).
// Typeface: Inter / Roboto — system SansSerif stack only. No monospace.
//
// Card corners at 12-16dp. Blue (#4C7DFF) is reserved for the Orion assistant.
// Zero elevation on all surfaces. Blurple accent on active/selected states.
//
// ─────────────────────────────────────────────────────────────────────────────

// ─── Color Palette — Grid visual direction ───────────────────────────────────

object AppColors {
    // Backgrounds & surfaces
    val Background       = Color(0xFF0B0B14)   // Main app background
    val Surface          = Color(0xFF151522)   // Cards, sheets, nav bars
    val SurfaceVariant   = Color(0xFF1B1B2B)   // Elevated surfaces, sidebars
    val SurfaceBorder    = Color(0xFF272739)   // Borders, outlines
    val InputBackground  = Color(0xFF10101A)   // Text inputs, console body
    val SurfaceOverlay   = Color(0xFF07070C)   // Overlays, modal backdrops

    // Primary accent — Grid violet
    val Primary          = Color(0xFF7C4DFF)   // Buttons, links, active states
    val PrimaryLight     = Color(0xFFA78BFA)   // Hover / highlighted Blurple
    val PrimaryDim       = Color(0xFF6237E0)   // Pressed Blurple
    val PrimarySubtle    = Color(0xFF7C4DFF).copy(alpha = 0.14f)  // Blurple tint bg

    // Semantic colors
    val Success          = Color(0xFF34D399)   // Online, confirmations
    val Warning          = Color(0xFFF5B841)   // Caution, moderate accents
    val Error            = Color(0xFFF23F43)   // Danger, offline, destructive
    val Info             = Color(0xFF4C7DFF)   // Informational (maps to Blurple)

    // Text tiers
    val TextPrimary      = Color(0xFFF5F5FA)   // Headers, high-emphasis body
    val TextSecondary    = Color(0xFFA3A7BD)   // Subtitles, descriptions
    val TextMuted        = Color(0xFF6E7290)   // Hints, timestamps, disabled
    val TextLink         = Color(0xFF8FB0FF)   // Hyperlinks

    // Utility tokens
    val Divider          = Color(0xFF272739)   // Horizontal/vertical separators
    val SwitchTrackOff   = Color(0xFF3A3A50)   // Inactive toggles
    val SliderInactive   = Color(0xFF3A3A50)   // Inactive slider tracks
    val ProgressTrack    = Color(0xFF3A3A50)   // Progress bar backgrounds

    // Bottom nav / tab bar tokens
    val NavBarBackground = Color(0xFF0F0F18)   // Bottom nav background
    val NavItemActive    = Color(0xFFFFFFFF)   // Active nav icon/label
    val NavItemInactive  = Color(0xFF6E7290)   // Inactive nav icon/label

    // Card module accent colors (for Tree/Library icons)
    val AccentRocket     = Color(0xFF34D399)   // Launch / Deploy
    val AccentShield     = Color(0xFFF5C451)   // Moderation / Safety
    val AccentBrain      = Color(0xFFE879F9)   // AI / Intelligence
    val AccentBolt       = Color(0xFF7C4DFF)   // Commands / Utilities
    val AccentMusic      = Color(0xFFF87171)   // Music / Audio
    val AccentGear       = Color(0xFF94A3B8)   // Settings / Config
    val AccentChart      = Color(0xFF22C55E)   // Analytics / Stats
    val AccentWelcome    = Color(0xFFF59E0B)   // Welcome / Onboarding
}

// ─── Material 3 Dark Color Scheme — Grid mapping ─────────────────────────────

private val AppDarkColorScheme = darkColorScheme(
    primary            = AppColors.Primary,
    onPrimary          = AppColors.TextPrimary,
    primaryContainer   = AppColors.PrimaryDim,
    onPrimaryContainer = AppColors.PrimaryLight,

    secondary          = AppColors.Info,
    onSecondary        = AppColors.TextPrimary,
    secondaryContainer = Color(0xFF232A5C),
    onSecondaryContainer = AppColors.Info,

    tertiary           = AppColors.Success,
    onTertiary         = AppColors.TextPrimary,
    tertiaryContainer  = Color(0xFF12482F),
    onTertiaryContainer = AppColors.Success,

    background         = AppColors.Background,
    onBackground       = AppColors.TextPrimary,

    surface            = AppColors.Surface,
    onSurface          = AppColors.TextPrimary,
    surfaceVariant     = AppColors.SurfaceVariant,
    onSurfaceVariant   = AppColors.TextSecondary,

    error              = AppColors.Error,
    onError            = AppColors.TextPrimary,
    errorContainer     = Color(0xFF5A1419),
    onErrorContainer   = AppColors.Error,

    outline            = AppColors.SurfaceBorder,
    outlineVariant     = Color(0xFF30304A)
)

// ─── Typography — Inter / Roboto Sans-Serif (Grid Professional Identity) ─────
// All text uses system SansSerif (Inter on Desktop, Roboto on Android).
// ZERO monospace anywhere. Clean, legible, modern — Grid brand direction.

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize   = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.25).sp,
        color = AppColors.TextPrimary
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize   = 28.sp,
        lineHeight = 36.sp,
        color = AppColors.TextPrimary
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize   = 24.sp,
        lineHeight = 32.sp,
        color = AppColors.TextPrimary
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 22.sp,
        lineHeight = 28.sp,
        color = AppColors.TextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 18.sp,
        lineHeight = 24.sp,
        color = AppColors.TextPrimary
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 22.sp,
        color = AppColors.TextPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp,
        lineHeight = 26.sp,
        color = AppColors.TextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 22.sp,
        color = AppColors.TextPrimary
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        color = AppColors.TextSecondary
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        color = AppColors.TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        lineHeight = 18.sp,
        color = AppColors.TextPrimary
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize   = 11.sp,
        lineHeight = 16.sp,
        color = AppColors.TextSecondary
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = AppColors.TextPrimary
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize   = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = AppColors.TextSecondary
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize   = 10.sp,
        lineHeight = 14.sp,
        color = AppColors.TextMuted
    )
)

// ─── Theme Composable ────────────────────────────────────────────────────────

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography  = AppTypography,
        content     = content
    )
}
