# Grid product summary

Grid is an Android app prototype for creating and managing Discord bots. Its Kotlin/Jetpack Compose client includes bot-management screens, while a separate FastAPI service exposes Redis-backed endpoints.

The shared dashboard direction is bot-specific: show the currently selected bot's identity and status instead of fixed sample account details. User-provided dashboard and launch screenshots may inspire Android layout decisions, but the repository does not contain a desktop/web client.

The client and backend are not connected yet. The Orion Assistant uses mock replies, and bot launch does not perform a real deployment. See the root `README.md` and `COORDINATION.md` for the current implementation status.
