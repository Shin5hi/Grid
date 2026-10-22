<div align="center">

# Grid
## Discord Bot Maker for Android

**Build, deploy, and manage Discord bots — from your phone.**

</div>

---

## Overview

Grid is a modern Android app for creating, testing, and managing Discord bots directly from a mobile device. Built with Kotlin and Jetpack Compose, Grid brings together bot creation, deployment, monitoring, and automation in a clean, premium mobile-first experience.

The product is designed to remove the friction of desktop-only bot management, giving creators a polished and efficient workflow from their phone.

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

Discord bot development is powerful, but often fragmented across desktop tools, APIs, and manual workflows. Grid brings that experience into one mobile-first platform with a premium design language and a practical user flow.

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
