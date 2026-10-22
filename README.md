<div align="center">

# 🚀 Grid
## <u>Discord Bot Maker for Android</u>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-68.9%25-7F52FF?style=for-the-badge&logo=kotlin" />
  <img src="https://img.shields.io/badge/Python-31.1%25-3776AB?style=for-the-badge&logo=python" />
  <img src="https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84?style=for-the-badge&logo=android" />
  <img src="https://img.shields.io/badge/FastAPI-Backend-009688?style=for-the-badge&logo=fastapi" />
  <img src="https://img.shields.io/badge/Redis-State%20Layer-DC382D?style=for-the-badge&logo=redis" />
</p>

**Build, deploy, and manage Discord bots — from your phone.**

</div>

---

## ✨ Overview

Grid is a modern Android app for creating, testing, and managing Discord bots directly from a mobile device. Built with Kotlin and Jetpack Compose, Grid brings together bot creation, deployment, monitoring, and command management in one polished mobile-first experience.

The goal is simple: remove the friction of desktop-only bot workflows and give creators a faster, cleaner, and more powerful way to build and manage bots from anywhere.

---

## 🎯 Mission

Grid exists to make Discord bot development accessible, fast, and elegant.

By combining a streamlined Android interface with backend orchestration and real-time status updates, Grid enables users to:

- 🧩 Build and manage bots from a phone
- 📊 Monitor logs and performance in real time
- 🛡️ Configure moderation and automation tools
- ⚙️ Deploy changes instantly
- 🌐 Run a complete bot ecosystem from one app

---

## 💡 Why Grid?

Discord bot development is powerful, but often fragmented across desktop tools, APIs, and manual workflows. Grid brings that experience into a single premium mobile platform with a polished visual language and simplified developer flow.

It is designed for:
- creators
- communities
- developers
- moderators
- advanced bot operators

who want speed, clarity, and control without sacrificing quality.

---

## 🧰 Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **📱 Frontend** | Kotlin + Jetpack Compose | Modern Android interface |
| **⚙️ Backend** | FastAPI (Python) | API and orchestration layer |
| **🔄 Realtime** | WebSockets | Log streaming and status updates |
| **🗄️ Cache / State** | Redis | Runtime state and event handling |
| **🧠 AI Moderation** | Google Gemini API | Content moderation and filtering |
| **🤖 Bot Runtime** | discord.py / Pycord | Discord bot execution |

---

## 🚀 Core Features

- **📊 Bot Dashboard** — real-time overview of bot status and activity
- **🖥️ Live Console** — monitor logs and runtime events as they happen
- **🧩 Command Builder** — design and manage bot commands visually
- **🤖 AI AutoMod** — moderation and content filtering with AI
- **⚙️ Deploy Workflow** — push updates and configuration changes directly from the app
- **🎵 Music Controls** — manage playback and audio actions
- **📈 Statistics** — track usage, performance, and operational metrics
- **🌑 Premium Dark UI** — a polished mobile-first design language

---

## 🏗️ Architecture

Grid is built as a multi-layer system designed to keep the experience smooth while supporting powerful backend orchestration.

- **📱 Android client** → dashboard, command builder, bot management
- **⚙️ Backend service** → orchestration, deployment flow, app logic
- **🔄 Real-time layer** → WebSocket-based log and status updates
- **🤖 Bot runtime** → Python-powered Discord bot execution
- **🧠 AI layer** → Gemini-powered moderation and filtering

This structure keeps the app lightweight while still supporting real-time workflows and scalable bot operations.

---

## 📁 Project Structure

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

## 🧪 Getting Started

### ✅ Prerequisites

Before running the project, make sure you have:

- 🧑‍💻 Android Studio
- ☕ JDK 17+
- 🟦 Kotlin configured in your environment
- 🐍 Python 3.10+
- ⚙️ FastAPI-compatible environment
- 🗄️ Redis server
- 🔑 Discord bot token
- 🧠 Google Gemini API key

---

### 1) Clone the repository

```bash
git clone https://github.com/Shin5hi/Grid.git
cd Grid
```

---

### 2) Android setup

Open the project in Android Studio and let Gradle sync the dependencies.

```bash
./gradlew assembleDebug
```

Then run the app from Android Studio on an emulator or physical device.

---

### 3) Backend setup

Navigate to the backend folder and install the required Python dependencies.

```bash
cd backend
python -m venv .venv
source .venv/bin/activate  # Windows: .venv\Scripts\activate
pip install -r requirements.txt
```

Create a `.env` file:

```env
DISCORD_TOKEN=your_discord_bot_token
GEMINI_API_KEY=your_gemini_key
REDIS_URL=redis://localhost:6379
BACKEND_PORT=8000
```

Then start the backend:

```bash
uvicorn main:app --reload
```

---

## 🔧 Development Notes

Grid is designed to be both developer-friendly and mobile-first. The Android layer is optimized for responsiveness and clarity, while the backend handles orchestration, deployment flow, real-time updates, and integrations.

This separation keeps the app lightweight while still supporting powerful bot workflows and scalable automation.

---

## 🤝 Contributing

Contributions are welcome.

1. 🍴 Fork the repository
2. 🌿 Create a feature branch
3. 📝 Commit your changes
4. 🔃 Open a pull request

Please keep changes focused, document new functionality clearly, and follow the project’s existing structure and conventions.

---

## 📜 License

This project is distributed under the repository’s chosen license. Please review the repository files for the exact licensing terms before use or distribution.

---

## 📬 Contact

- GitHub: https://github.com/Shin5hi
- Repository: https://github.com/Shin5hi/Grid

---

<div align="center">

**🚀 Grid is built for creators who want to move faster, stay in control, and manage Discord bots from anywhere.**

</div>
