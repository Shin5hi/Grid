# 🤝 Grid & Codex Collaboration Board

## ⚠️ DIRECT ORDER: 100% VISUAL FIDELITY
The boss has approved the definitive master mockup.

**Codex / Sprite / Gaul:**
No aesthetic changes are allowed unless they are present in `design/OFFICIAL_MOCKUP.md`.
- The backend must support the density of data shown (Projects, Tasks, Tools, Notes).
- The frontend (Android) must be a clone of this image.

Time to work! 🚀

---

## Migration Status

The Android frontend migration is in progress. Current status:

- **pending / waiting**: the Billing screen remains pending implementation (placeholder).
- Organization permissions (org permissions): each server belongs to an organization and its permissions are resolved by role. Pending connection of the org-level backend permissions.
- Personal fork of the author: `Shin5hi/discord-bot-maker-android`.

## Server Tree View

The tools screen uses a Discord-style hierarchical **Tree View**:

- Collapsible categories (compact / high-density) with branch lines drawn via `Canvas` + `drawLine` to connect parents and children.
- Data types `TreeChannel` and `TreeCategory` in `ui/library/ToolLibraryScreen.kt`.
- High-density layout so many channels can fit on screen.

## Orion Assistant (OrionBubble FAB)

- `ui/components/OrionBubble.kt`: draggable floating action button (`pointerInput` + `detectDragGestures`) with antennas, eyes, and pulse animation (`rememberInfiniteTransition`).
- Integrated into the `Scaffold` in `ui/navigation/AppNavigation.kt`; it navigates to the Orion Assistant (`DoubtAssistantScreen`).
- Hidden on the `SPLASH` route (and on the Orion screen itself) via the `hideOrionRoutes` set and the `showOrionBubble` flag.

## Stats Dashboard Module

Bot metrics module.

- **File (ownership, status Active):** `ui/StatsDashboardScreen.kt`
- **Path:** `stats_dashboard`
- **Models:** `BotStats`, `ActivityDataPoint`
- **Components:** `StatsCardGrid` / `StatCard`, `ActivityChart`
- Follows the art direction: flat cards with elevation `0.dp` and corners `8.dp`.

## Art Direction

- Primary Blurple: `#5865F2`
- Charcoal background: `#313338`
- Surface: `#2B2D31`
- Flat cards: elevation `0.dp`, corners `8.dp`.

## Conventions

- All colors are referenced via `AppColors.*`; never inline hex colors on screens.
- Default Material typography (nothing like `FontFamily.Monospace`).
- Kotlin code in the source set `app/src/main/kotlin/...` with one package per screen.

## File Ownership

| File | Owner | Status |
|---|---|---|
| `app/src/main/kotlin/.../ui/theme/AppTheme.kt` | Codex | Active |
| `app/src/main/kotlin/.../ui/theme/NeonColors.kt` | Codex | Active |
| `app/src/main/kotlin/.../ui/dashboard/MainDashboardScreen.kt` | Sprite | Active |
| `app/src/main/kotlin/.../ui/navigation/AppNavigation.kt` | Sprite | Active |
| `app/src/main/kotlin/.../ui/components/OrionBubble.kt` | Gaul | Active |
| `app/src/main/kotlin/.../ui/library/ToolLibraryScreen.kt` | Gaul | Active |
| `ui/StatsDashboardScreen.kt` | Sprite | Active |
| `ui/splash/OriginLoadingScreen.kt` | Sprite | Active |
| `backend_api.py` | Codex | Active |
