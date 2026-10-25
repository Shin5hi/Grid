# Grid project summary

Grid is an Android application prototype for managing Discord bot workflows. The repository contains a Kotlin/Jetpack Compose client and a separate FastAPI backend that uses Redis for selected state and event delivery.

The Android UI currently operates on local/default state and is not connected to the backend. The backend provides API endpoints for logs, AutoMod configuration, music queue actions, command definitions, health checks, and a simulated bot-provisioning request. A live Discord bot runtime, Gemini integration, and cloud deployment are not implemented.

See `README.md` for setup, build, test, and API details, and `ESTRUCTURA_OFICIAL.md` for the current source layout.
