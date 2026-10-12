# Grid — Brand Assets & Visual Specification

Current direction, set on 2026-10-12 from the approved mockups. This file replaces the
earlier Discord-blurple spec. The in-app values live in `ui/theme/AppTheme.kt`
(`AppColors`) and `res/values/colors.xml`. Keep them in sync with this file.

## Name

- **Display name:** Grid
- **Attribution line:** by Shin5hi
- **Slogan:** Crea, Organiza, Avanza.

## Logo

- **Mark:** a bold, geometric violet "G" (`#7C4DFF`), drawn as a vector.
  - In-app: `res/drawable/ic_grid_logo.xml`
  - Launcher: `res/drawable/ic_launcher_foreground.xml` on a light background
    (`launcher_background`, `#F4F4F6`), scaled to the 66dp safe zone.
- The vector is traced from the approved icon. It does not include the faint grid lines
  of the original artwork. If an exact export is provided, replace the path data.

## Color tokens

| Token              | Hex       | Role                                        |
|--------------------|-----------|---------------------------------------------|
| Background         | `#0B0B14` | Screen background (Deep Charcoal navy)      |
| Surface            | `#151522` | Cards, bars                                 |
| SurfaceVariant     | `#1B1B2B` | Raised rows, icon tiles                     |
| SurfaceBorder      | `#272739` | Borders, dividers                           |
| InputBackground    | `#10101A` | Inputs, console body                        |
| Primary (violet)   | `#7C4DFF` | Brand, CTAs, active states, name highlight  |
| PrimaryLight       | `#A78BFA` | Hover and highlights                        |
| Info (blue)        | `#4C7DFF` | Orion assistant bubble                      |
| Success            | `#34D399` | Online, confirmations                       |
| Warning            | `#F5B841` | Caution                                     |
| Error              | `#F23F43` | Danger                                      |
| TextPrimary        | `#F5F5FA` | Headings                                    |
| TextSecondary      | `#A3A7BD` | Descriptions, subtitles                     |
| TextMuted          | `#6E7290` | Hints, footer, inactive nav                 |

Values are estimated from the approved screenshots. Adjust them if a design export is provided.

## Layout

- **Dashboard:** header with the G mark and wordmark, then the greeting
  "Hola, <name>" with the name in violet, then "¿Qué vas a crear hoy?". Below that:
  the status card, then "ACCESO RÁPIDO" as a list of rows (icon tile, title,
  description, chevron).
- **Bottom navigation:** Inicio, The Tree, Ajustes.
- **Corners:** 12–16dp on cards and rows, 10dp on icon tiles.
- **Typography:** system sans-serif only. No monospace.

## Loading screen

- Logo: the G mark (96dp), with a rotating sweep ring around it (140dp, 3dp stroke,
  violet, 1400ms per revolution).
- Title: "Grid", 32sp, bold.
- Background: `Background` (`#0B0B14`).

## Orion assistant

- Floating bubble in Info blue (`#4C7DFF`) with a soft halo and a pulse.
- Draggable. Opens the assistant screen.
