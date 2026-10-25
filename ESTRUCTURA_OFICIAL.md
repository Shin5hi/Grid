# Current project structure

This document describes the repository as it exists today. It replaces earlier implementation plans and completion estimates, which no longer match the source tree.

## Android application

The Android application is a Kotlin and Jetpack Compose project under `app/src/main/kotlin/com/discordbotmaker/android/`.

| Area | Current location | Notes |
|---|---|---|
| Entry point | `MainActivity.kt` | Applies the app theme and mounts the navigation graph |
| Navigation | `ui/navigation/AppNavigation.kt` | Defines routes and screen transitions |
| Theme | `ui/theme/AppTheme.kt` | Shared app colors and Material theme |
| Dashboard | `ui/dashboard/MainDashboardScreen.kt` | Dashboard UI with local view-model state |
| Tool library | `ui/library/ToolLibraryScreen.kt` | Collapsible, searchable tool tree |
| Commands | `ui/commands/CommandBuilderScreen.kt` | Command editor UI |
| AutoMod | `ui/automod/AutoModScreen.kt` | Moderation settings UI |
| Bot launch | `ui/launch/BotCreationScreen.kt` | Token-format validation and launch flow UI |
| Console | `ui/console/LiveConsoleScreen.kt` | In-memory log list and connection state |
| Assistant | `ui/doubt/DoubtAssistantScreen.kt` | Local assistant UI |
| Statistics | `ui/stats/StatsDashboardScreen.kt` | Statistics cards and activity chart UI |
| Music | `ui/music/MusicPlayerScreen.kt` | Music controls UI |
| Components | `ui/components/OrionBubble.kt` | Draggable assistant shortcut |
| Splash/loading | `ui/splash/` | Splash wrapper and animated loading screen |

The Android source currently has no HTTP or WebSocket client calls. Screen state and displayed metrics are supplied locally or by defaults; these features should not be described as integrated with live Discord or backend data.

## Backend

`backend_api.py` at the repository root is the module used by the test suite and the documented Uvicorn command. `backend/backend_api.py` is a mirrored copy. Both define the FastAPI application, Redis-backed settings/queue/command storage, WebSocket log stream, and simulated bot provisioning endpoint.

The backend requires Redis during application startup. It reads `REDIS_URL` and `LOG_CHANNEL`; it does not provision cloud infrastructure, run a Discord bot, or invoke Gemini.

## Build and validation

- Android SDK: compile/target SDK 35; minimum SDK 26.
- Java: JDK 17.
- Android build: `./gradlew :app:assembleDebug` (Windows: `.\gradlew.bat :app:assembleDebug`).
- Python tests: `python -m pytest -q`.
- Backend live-import test: requires FastAPI and its runtime dependencies.

## Repository maintenance

The `scripts/cleanup_repo.sh` utility deletes root `test_*.py` files and named legacy documents. Read `scripts/README.md` and inspect the script before using it.
