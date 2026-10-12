package com.discordbotmaker.android.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Accent palette used by the Console, Command Builder, Bot Creation and AutoMod screens.
 *
 * Every value is mapped to an existing [AppColors] token so the app keeps a single
 * source of truth for the Discord HiFi palette (see design/OFFICIAL_BRANDING.md).
 * Do not introduce raw hex values here.
 */
object NeonColors {
    val Background: Color = AppColors.Background
    val InputBackground: Color = AppColors.InputBackground
    val SurfaceCard: Color = AppColors.Surface
    val SurfaceBorder: Color = AppColors.SurfaceBorder

    val TextPrimary: Color = AppColors.TextPrimary
    val TextSecondary: Color = AppColors.TextSecondary
    val TextDim: Color = AppColors.TextMuted

    val NeonCyan: Color = AppColors.TextLink
    val NeonGreen: Color = AppColors.AccentRocket
    val NeonMagenta: Color = AppColors.AccentBrain
    val NeonPurple: Color = AppColors.PrimaryLight
    val NeonRed: Color = AppColors.AccentMusic
}
