package com.discordbotmaker.android.ui.theme

import androidx.compose.ui.graphics.Color

// ─── NeonColors ─────────────────────────────────────────────────────────────
//
// Neon palette used by the Command Builder and Bot Creation screens.
// It previously lived implicitly in those screens but was never declared,
// which broke compilation. It is centralized here in the `theme` package,
// consistent with the dark palette used by AppColors.
//
// ───────────────────────────────────────────────────────────────────────────

object NeonColors {
    // Backgrounds and surfaces
    val Background      = Color(0xFF131417)
    val SurfaceCard     = Color(0xFF1E2024)
    val SurfaceBorder   = Color(0xFF2E3136)
    val InputBackground = Color(0xFF121316)

    // Neon accents
    val NeonPurple      = Color(0xFF5865F2)
    val NeonCyan        = Color(0xFF00E5FF)
    val NeonGreen       = Color(0xFF57F287)
    val NeonRed         = Color(0xFFED4245)
    val NeonMagenta     = Color(0xFFEB459E)

    // Text
    val TextPrimary     = Color(0xFFFFFFFF)
    val TextSecondary   = Color(0xFFB5BAC1)
    val TextDim         = Color(0xFF80848E)
}
