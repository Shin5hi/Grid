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

/**
 * Grid Branding Identity
 *
 * Minimalist geometric "G" logo mark — a single continuous stroke forming
 * a stylised "G" with an open counter, suggesting both a chat bubble and
 * a circuit trace. Rendered in Discord Blurple (#5865F2) on Deep Charcoal
 * (#313338) backgrounds. The mark is monochrome, never outlined, never
 * gradient-filled. It appears at 24 dp in the navigation rail, 32 dp on
 * splash, and 48 dp as app icon.
 *
 * Colour vocabulary: Blurple primary, Deep Charcoal background,
 * Dark Surface cards, White/Secondary text, Green success, Red danger,
 * Amber warning, Cyan info. Accent chips (Rocket, Shield, Brain, Bolt,
 * Music, Gear, Chart, Welcome) use semantic neon tints at 12% opacity.
 *
 * Typography: Inter / Roboto (system SansSerif) only. No Monospace in UI.
 * Corner radius: 8 dp cards, 12 dp primary buttons, 20 dp modals.
 * Elevation: flat (tonalElevation = 0.dp) on all headers and surfaces.
 * Motion: 150–200 ms standard easing; no bounce, no elastic.
 */
object AppColors {
    // Grid brand palette from the current dark mockup.
    val Background       = Color(0xFF0A0A0F)
    val Surface          = Color(0xFF14151B)
    val SurfaceVariant   = Color(0xFF1C1E26)
    val SurfaceBorder    = Color(0xFF26282F)
    val InputBackground  = Color(0xFF0F1015)
    val SurfaceOverlay   = Color(0xFF000000)

    // Grid purple from the G logo.
    val Primary          = Color(0xFF7C5CFF)
    val PrimaryLight     = Color(0xFFA78BFA)
    val PrimaryDim       = Color(0xFF5B3DF0)
    val PrimarySubtle    = Color(0xFF7C5CFF).copy(alpha = 0.12f)

    // Secondary blue accent from the mockup.
    val SecondaryBlue    = Color(0xFF5B8DEF)

    val Success          = Color(0xFF22C55E)
    val Warning          = Color(0xFFF59E0B)
    val Error            = Color(0xFFEF4444)
    val Info             = Color(0xFF5B8DEF)

    val TextPrimary      = Color(0xFFFFFFFF)
    val TextSecondary    = Color(0xFF9CA3AF)
    val TextMuted        = Color(0xFF6B7280)
    val TextLink         = Color(0xFF5B8DEF)

    val Divider          = Color(0xFF26282F)
    val SwitchTrackOff   = Color(0xFF2A2D36)
    val SliderInactive   = Color(0xFF2A2D36)
    val ProgressTrack    = Color(0xFF2A2D36)

    val NavBarBackground = Color(0xFF0F1015)
    val NavItemActive    = Color(0xFF7C5CFF)
    val NavItemInactive  = Color(0xFF6B7280)

    val AccentRocket     = Color(0xFF57F287)
    val AccentShield     = Color(0xFFFEE75C)
    val AccentBrain      = Color(0xFFEB459E)
    val AccentBolt       = Color(0xFF5865F2)
    val AccentMusic      = Color(0xFFED4245)
    val AccentGear       = Color(0xFF99AAB5)
    val AccentChart      = Color(0xFF3BA55D)
    val AccentWelcome    = Color(0xFFFAA81A)
}

private val AppDarkColorScheme = darkColorScheme(
    primary            = AppColors.Primary,
    onPrimary          = AppColors.TextPrimary,
    primaryContainer   = AppColors.PrimaryDim,
    onPrimaryContainer = AppColors.PrimaryLight,
    secondary          = AppColors.SecondaryBlue,
    onSecondary        = AppColors.TextPrimary,
    secondaryContainer = Color(0xFF1E2547),
    onSecondaryContainer = AppColors.SecondaryBlue,
    tertiary           = AppColors.Success,
    onTertiary         = AppColors.TextPrimary,
    tertiaryContainer  = Color(0xFF0F2E1F),
    onTertiaryContainer = AppColors.Success,
    background         = AppColors.Background,
    onBackground       = AppColors.TextPrimary,
    surface            = AppColors.Surface,
    onSurface          = AppColors.TextPrimary,
    surfaceVariant     = AppColors.SurfaceVariant,
    onSurfaceVariant   = AppColors.TextSecondary,
    error              = AppColors.Error,
    onError            = AppColors.TextPrimary,
    errorContainer     = Color(0xFF3A1113),
    onErrorContainer   = AppColors.Error,
    outline            = AppColors.SurfaceBorder,
    outlineVariant     = Color(0xFF1C1E26)
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 44.sp, letterSpacing = (-0.25).sp, color = AppColors.TextPrimary),
    displayMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp, color = AppColors.TextPrimary),
    displaySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp, color = AppColors.TextPrimary),
    headlineLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp, color = AppColors.TextPrimary),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp, color = AppColors.TextPrimary),
    headlineSmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 22.sp, color = AppColors.TextPrimary),
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp, color = AppColors.TextPrimary),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 22.sp, color = AppColors.TextPrimary),
    titleSmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, color = AppColors.TextSecondary),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, color = AppColors.TextPrimary),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp, color = AppColors.TextPrimary),
    bodySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp, color = AppColors.TextSecondary),
    labelLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.5.sp, color = AppColors.TextPrimary),
    labelMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp, color = AppColors.TextSecondary),
    labelSmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 10.sp, lineHeight = 14.sp, color = AppColors.TextMuted)
)

/**
 * Applies Grid's dark Material theme to [content].
 *
 * @param content Composable content rendered inside the theme.
 */
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AppDarkColorScheme, typography = AppTypography, content = content)
}
