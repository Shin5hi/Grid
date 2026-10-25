<div align="center">

# Grid
## Discord Bot Maker for Android

**A mobile-first Android interface and an experimental Discord bot management API.**

</div>

---

## Project status

Grid currently contains an Android UI prototype and a separate FastAPI backend prototype. The Android screens use local/default state: the app does not yet make HTTP or WebSocket requests to the backend. The dashboard can display a selected bot's `BotStatus` values, but bot creation/selection is not yet connected to that state. Backend endpoints are not proof of a live Discord bot runtime or cloud deployment; deployment currently records a provisioning request and returns a simulated provisioning response.

The dashboard is intended to be scoped to whichever bot is created or selected, with bot-specific identity and metrics rather than fixed sample counters. User-provided dashboard and launch screenshots are visual inspiration for the Android experience; the project remains Android-only and does not include a desktop/web client.

## Features in the repository

The Android app includes navigation, a dashboard, a tool library, command and AutoMod editors, a bot launch flow, a log console, statistics, a music-player screen, and the Orion Assistant screen. These screens are UI prototypes and are not connected to persistent backend data yet.

The backend exposes:

| Method | Path | Purpose |
|---|---|---|
| WebSocket | `/ws/logs` | Stream log events published through Redis Pub/Sub |
| `POST` | `/api/logs` | Publish a validated log entry |
| `GET`, `PUT` | `/api/automod/config` | Read or store AutoMod settings in Redis |
| `GET` | `/health` | Check Redis connectivity |
| `POST` | `/api/music/play` | Publish a play or resume command |
| `GET` | `/api/music/queue` | Read the queue and current track from Redis |
| `POST` | `/api/music/add` | Add a track to the Redis queue |
| `POST` | `/api/bots/deploy` | Validate a token and create a simulated provisioning request |
| `GET`, `POST`, `DELETE` | `/api/commands` | List, save, or delete command definitions in Redis |

Interactive API documentation is available at `/docs` while the backend is running.

## Technology

- **Android:** Kotlin, Jetpack Compose, Material 3, Navigation Compose
- **Build:** Gradle wrapper, Android Gradle Plugin, JDK 17
- **Backend:** Python, FastAPI, Pydantic, Redis asyncio client
- **Tests:** pytest-based repository checks; backend live-import checks require FastAPI

The Android module targets SDK 35 and supports Android 8.0 (API 26) and later.

## Repository layout

```text
Grid/
├── app/src/main/
│   ├── kotlin/com/discordbotmaker/android/  # Android application and UI
│   └── res/                                 # Android resources
├── backend_api.py                           # FastAPI application
├── backend/backend_api.py                   # Mirrored backend source
├── scripts/                                 # Repository maintenance scripts
├── design/                                  # Visual and branding references
├── test_*.py                                # Python repository tests
├── gradlew
├── gradlew.bat
└── README.md
```

## Requirements

- Android Studio with Android SDK 35
- JDK 17
- Python 3.10 or later
- Redis for running the backend

The backend reads `REDIS_URL` (default: `redis://localhost:6379/0`) and `LOG_CHANNEL` (default: `bot:logs`). It does not currently read `DISCORD_TOKEN`, `GEMINI_API_KEY`, or `BACKEND_PORT` from the environment.

## Build the Android app

Open the repository in Android Studio and allow Gradle to sync, or run:

```bash
# macOS / Linux
./gradlew :app:assembleDebug

# Windows PowerShell
.\gradlew.bat :app:assembleDebug
```

Run the app on an emulator or Android device from Android Studio.

## Run the backend

From the repository root, install the backend packages and start Redis. Then run:

```bash
python -m venv .venv

# macOS / Linux
source .venv/bin/activate

# Windows PowerShell
.\.venv\Scripts\Activate.ps1

python -m pip install fastapi uvicorn redis pydantic
uvicorn backend_api:app --host 0.0.0.0 --port 8000 --reload
```

The backend checks its Redis connection during startup. Open `http://localhost:8000/docs` for the generated API reference and `http://localhost:8000/health` for the health endpoint.

## Run repository tests

```bash
python -m pip install pytest
python -m pytest -q
```

The backend import test is skipped when FastAPI is not installed. The repository also contains structural and source-based tests for Android UI files.

## Notes for contributors

- Keep Android code under `app/src/main/kotlin/com/discordbotmaker/android/`.
- Keep application colors centralized in `ui/theme/AppTheme.kt`.
- Update this README when API behavior, build requirements, or the integration status changes.
- `scripts/cleanup_repo.sh` deletes root-level `test_*.py` files and several named legacy documents. Review the script before running it; it is a destructive maintenance utility, not a test or build command.

## License

No license file is currently present. Ask the repository maintainers before redistributing this project.
