# Grid Origin — Brand Assets & Logo Specification

## Official Logo

**Asset Name:** Grid Origin Logo
**Shape:** Rounded square (20dp corner radius) with solid Grid Purple (`#7C5CFF`) fill.
**Glyph:** Bold uppercase **"G"** in white (#FFFFFF), centered within the square. Set at 48sp, `FontWeight.Bold`, system sans-serif (Inter / Roboto).
**Dimensions:** 96×96dp logical size. Export at 1×, 1.5×, 2×, 3×, 4× density buckets for Android (`mdpi` through `xxxhdpi`).

### Loading Animation Ring
A continuous sweep ring orbits the logo during the loading screen:
- **Ring diameter:** 140dp (centered around the 96dp logo with 22dp clearance per side).
- **Stroke:** 3dp, round cap.
- **Color:** Grid Purple (`#7C5CFF`) with gradient opacity sweep (transparent → 60% → 100%).
- **Background track:** Grid Purple at 10% opacity, full 360° circle.
- **Rotation speed:** 1400ms per full revolution, linear easing, infinite repeat.
- **Glow pulse:** Ring leading-edge alpha oscillates between 25%–60% at half the rotation period.

## Brand Name

**Full Name:** Grid Origin
**Display Font:** System sans-serif (`FontFamily.SansSerif`), 32sp, `FontWeight.Bold`, `letterSpacing = 1.sp`.
**Color:** White (`#FFFFFF`) on charcoal background.

## Slogan

**Text:** "Create, Organize, Move Forward."
**Display Font:** System sans-serif, 14sp, `FontWeight.Normal`, `letterSpacing = 2.sp`.
**Color:** `TextSecondary` (`#9CA3AF`).

## Background

**Loading Screen Background:** `AppColors.Background` (`#0A0A0F`).

## Color Tokens (Summary)

| Token            | Hex       | Role                          |
|------------------|-----------|-------------------------------|
| Primary purple   | `#7C5CFF` | Logo fill, sweep ring, CTAs   |
| Deep charcoal    | `#0A0A0F` | Loading screen background     |
| White            | `#FFFFFF` | Logo glyph, title text        |
| TextSecondary    | `#9CA3AF` | Slogan text                   |
| TextMuted @40%   | `#6B7280` | Version stamp                 |

## Source Image Reference

The official Grid Origin logo was provided by the user on 2026-05-03:
`https://customer-assets.emergentagent.com/wingman/85fd1fa5-923c-402f-b2c2-f78866b3e4b5/attachments/d0e5ec43108c454e995393a525f67b57_photo.jpg`

This is a 2D blue robot with antenna dots and a `>_` prompt symbol. The in-app representation uses a simplified geometric "G" glyph as the scalable vector equivalent.
