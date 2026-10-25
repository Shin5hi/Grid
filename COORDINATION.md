# Project development notes

## Current implementation

- Android app source lives under `app/src/main/kotlin/com/discordbotmaker/android/`.
- The app uses Kotlin, Jetpack Compose, Material 3, and Navigation Compose.
- The backend entry point is the root `backend_api.py`; it uses FastAPI and Redis.
- The backend and Android app are not integrated yet. The assistant uses mock responses, console data is in memory, and the launch callback must be supplied by the caller.
- Bot deployment is simulated by the backend; it does not provision cloud resources or start a Discord bot.

## Bot dashboard direction

The dashboard is for whichever bot is selected or created in the app. It must not be tied to a developer username or hard-coded sample counters. Use the selected bot's name, online state, server/member counts, uptime, and latency from `BotStatus`.

The user-provided dashboard and launch screenshots are visual inspiration for the Android experience, not a request to add a desktop/web client. Adapt their hierarchy and clarity to the Android layout. Do not show sample metrics as live data.

The current screen can render bot data passed to it, but bot creation/selection is not wired to the dashboard yet. Keep this limitation explicit until the app persists and supplies the selected bot's actual data.

## Shared screens

- `app/src/main/kotlin/com/discordbotmaker/android/ui/splash/OriginLoadingScreen.kt` provides the animated Grid Origin loading screen and `SweepLoadingRing`.
- `SplashScreen` delegates to the origin loading screen; navigation then moves to the dashboard.
- `app/src/main/kotlin/com/discordbotmaker/android/ui/stats/StatsDashboardScreen.kt` contains the statistics cards and activity chart prototype.
- `app/src/main/kotlin/com/discordbotmaker/android/ui/components/OrionBubble.kt` is the draggable assistant shortcut.
- The loading-screen slogan is "Create, Organize, Move Forward."

## Source conventions

- Keep Android production code under `app/src/main/kotlin/com/discordbotmaker/android/`, organized by screen or component.
- Use `AppColors` and the shared theme for application colors.
- Use English for source comments, API documentation, and user-facing strings.
- Add KDoc to public composables and docstrings to public backend functions.
- Keep the two backend source copies synchronized when changing backend behavior.

## Validation

- Android build: `./gradlew :app:assembleDebug` (Windows: `.\gradlew.bat :app:assembleDebug`).
- Python tests: `python -m pytest -q`.
- Redis is required to run the FastAPI application.

The cleanup script deletes root-level test files. Review `scripts/README.md` and the script itself before running it.
