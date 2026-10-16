package com.discordbotmaker.android.ui.theme

import androidx.compose.ui.graphics.Color

// ─── NeonColors ────────────────────────────────────────────────────────────
//
// Paleta "neon" usada por las pantallas de Command Builder y Bot Creation.
// Antes vivía implícita en esas pantallas pero nunca fue declarada, lo que
// rompía la compilación. Se centraliza aquí en el paquete `theme`, coherente
// con la paleta oscura de AppColors.
//
// ────────────────────────────────────────────────────────────────────────────

object NeonColors {
    // Fondos y superficies
    val Background      = Color(0xFF131417)
    val SurfaceCard     = Color(0xFF1E2024)
    val SurfaceBorder   = Color(0xFF2E3136)
    val InputBackground = Color(0xFF121316)

    // Acentos neon
    val NeonPurple      = Color(0xFF5865F2)
    val NeonCyan        = Color(0xFF00E5FF)
    val NeonGreen       = Color(0xFF57F287)
    val NeonRed         = Color(0xFFED4245)
    val NeonMagenta     = Color(0xFFEB459E)

    // Texto
    val TextPrimary     = Color(0xFFFFFFFF)
    val TextSecondary   = Color(0xFFB5BAC1)
    val TextDim         = Color(0xFF80848E)
}
