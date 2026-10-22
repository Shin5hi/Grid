<div align="center">

# Grid
## Discord Bot Maker for Android

**Build, deploy, and manage Discord bots — from your phone.**

</div>

---

## Overview

Grid is a modern Android app for creating, testing, and managing Discord bots directly from a mobile device. Built with Kotlin and Jetpack Compose, Grid brings together bot creation, deployment, monitoring, and command management in one polished mobile experience.

The product is designed to remove the friction of desktop-only bot management, giving creators a streamlined workflow that feels fast, clear, and powerful from their phone.

---

## Mission

Grid exists to make Discord bot development accessible, fast, and elegant.

By combining a streamlined Android interface with backend orchestration and real-time status updates, Grid enables users to:
- build and manage bots from mobile
- monitor logs and performance in real time
- configure moderation and automation tools
- deploy changes quickly
- operate a bot ecosystem from a single app

---

## Why Grid

Discord bot development is powerful, but often fragmented across desktop tools, APIs, and manual workflows. Grid brings that experience into one mobile-first platform with a premium design language and a simplified developer workflow.

It is built for creators, communities, and developers who want clarity, control, and speed without sacrificing quality.

---

## Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **Frontend** | Kotlin + Jetpack Compose | Modern Android interface |
| **Backend** | FastAPI (Python) | API and orchestration layer |
| **Realtime** | WebSockets | Log streaming and status updates |
| **State / Cache** | Redis | Event handling and runtime state |
| **AI Moderation** | Google Gemini API | Content moderation and filtering |
| **Bot Runtime** | discord.py / Pycord | Discord bot execution |

---

## Core Features

- **Bot dashboard** — real-time overview of bot status and activity
- **Live console** — monitor logs and runtime events as they happen
- **Command builder** — design and manage bot commands visually
- **AI AutoMod** — moderation tools and content filtering
- **Deploy workflow** — push updates and configuration directly from the app
- **Music controls** — manage playback and audio-related bot actions
- **Statistics** — track bot usage and operational metrics
- **Premium dark UI** — modern Android experience built for mobile

---

## Architecture

Grid is designed as a multi-layer platform:

- Android client: user interface, dashboards, command building, bot management
- Backend service: orchestration, bot lifecycle, telemetry, deployments
- Real-time layer: status and log streaming via WebSockets
- Runtime environment: Python-based Discord bots running logic and integrations
- AI moderation: content analysis and filtering using Gemini

This structure keeps the app mobile-friendly while allowing powerful backend orchestration and scalable bot operations.

---

## Project Structure

```text
Grid/
├── README.md
├── app/
├── backend/
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── .gitignore
├── ESTRUCTURA_OFICIAL.md
├── COORDINATION.md
└── ...
```

---

## Getting Started

### Prerequisites

Before running the project, make sure you have:

- Android Studio
- JDK 17+
- Kotlin configured in Android Studio
- Python 3.10+
- FastAPI-compatible environment
- Redis server
- Discord bot token
- Google Gemini API key

### 1) Clone the repository

```bash
git clone https://github.com/Shin5hi/Grid.git
cd Grid
```

### 2) Android app setup

Open the project in Android Studio and sync Gradle files.

```bash
./gradlew assembleDebug
```

### 3) Backend setup

Navigate to the backend directory and install Python dependencies.

```bash
cd backend
python -m venv .venv
source .venv/bin/activate  # Windows: .venv\Scripts\activate
pip install -r requirements.txt
```

Create your environment variables for the backend, such as:

```env
DISCORD_TOKEN=your_discord_bot_token
GEMINI_API_KEY=your_gemini_key
REDIS_URL=redis://localhost:6379
BACKEND_PORT=8000
```

Then run the backend:

```bash
uvicorn main:app --reload
```

---

## Features in Detail

### Bot Dashboard
Monitor the health, state, and activity of your Discord bots from a mobile-first dashboard.

### Live Console
View real-time logs, events, and runtime activity while your bot is running.

### Command Builder
Configure commands, triggers, and actions without relying on a full desktop setup.

### AI AutoMod
Use AI-assisted moderation to filter content and protect communities more efficiently.

### Deploy Workflow
Push updates, configuration changes, and bot logic from your phone with a streamlined deployment flow.

### Music Controls
Manage playback and audio-based commands from the app for music-enabled bots.

### Statistics
Track usage and operational metrics to understand demand and performance.

---

## Development Notes

Grid is intended to be both developer-friendly and mobile-first. The Android layer is focused on clarity and responsiveness, while the backend handles orchestration, Redis state, and bot lifecycle operations.

This separation helps keep the app lightweight while still supporting real-time bot workflows and automation.

---

## Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Open a pull request

Please keep changes focused, document new functionality clearly, and follow the project’s existing structure and code conventions.

---

## License

This project is currently distributed under the repository’s chosen license. Please check the repository files for the exact license terms before use or distribution.

---

## Contact

- GitHub: https://github.com/Shin5hi
- Repository: https://github.com/Shin5hi/Grid

---

Grid is built for creators who want to move faster, stay in control, and manage Discord bots from anywhere.
