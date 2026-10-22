# 📐 OFFICIAL PROJECT STRUCTURE - Grid Discord Bot Maker

**Based on**: `design/OFFICIAL_MOCKUP.md` + `design/branding.md` + `design/visual-tree.md`

---

## 🎯 OVERVIEW

This is an Android project that is **100% designed** but **50% implemented**.

**Official Mockup**: https://customer-assets.emergentagent.com/wingman/85fd1fa5-923c-402f-b2c2-f78866b3e4b5/attachments/5c703d3ae1ea419b82c37f9ef848a102_photo.jpg

---

## 🏗️ REQUIRED OFFICIAL ARCHITECTURE

### **Correct Navigation Flow**

```
SplashScreen (2-3 sec)
    ↓
OriginLoadingScreen (Grid Origin animated logo)
    ↓
MainDashboardScreen (central hub - "Hello, Shin5hi")
    ├─→ Projects (Counter)
    ├─→ Tasks (Counter)
    ├─→ Tools (Counter)
    ├─→ Notes (Counter)
    └─→ Quick actions:
        ├─ CommandBuilderScreen
        ├─ AutoModScreen
        ├─ BotCreationScreen
        ├─ LiveConsoleScreen
        └─ MusicPlayerScreen

    + ToolLibraryScreen (Right sidebar / split view)
        ├─ ORGANIZE (AutoMod, Logs, Security)
        ├─ CREATE (Welcome, Polls, Giveaways)
        ├─ AUTOMATE (Levels, Ranks, Shop)
        └─ DEVELOP (Custom Commands, Tasks)

DoubtAssistantScreen (Orion Assistant - modal/push)
StatsDashboardScreen (Statistics - new)
```

---

## 🎨 IDENTITY SYSTEM (MANDATORY)

### **Colors - Discord Official HiFi**

```kotlin
object AppColors {
    // Backgrounds (Discord Dark Theme)
    Background       = #313338  // Main
    Surface          = #2B2D31  // Cards
    SurfaceVariant   = #232428  // Elevated
    InputBackground  = #1E1F22  // Console/Inputs

    // PRIMARY ACCENT
    Primary          = #5865F2  // Blurple (Grid Brand Color)
    PrimaryLight     = #7984F5
    PrimaryDim       = #4752C4

    // Semantic
    Success          = #23A559
    Error            = #F23F43
    Warning          = #FAA81A

    // Text Hierarchy
    TextPrimary      = #FFFFFF
    TextSecondary    = #B5BAC1
    TextMuted        = #80848E
}
```

### **Typography**
- **Font**: System SansSerif ONLY (Inter/Roboto)
- **NO Monospace** anywhere
- **Display**: Bold 36sp
- **Heading**: SemiBold 22sp
- **Body**: Normal 14sp

---

## 📱 REQUIRED SCREENS (CANONICAL PATHS)

| Screen | Path | Status | Notes |
|--------|------|--------|-------|
| **SplashScreen** | `ui/splash/SplashScreen.kt` | ❌ Missing | 2-3 sec entry point |
| **OriginLoadingScreen** | `ui/splash/OriginLoadingScreen.kt` | ⚠️ Exists under `ui/` | Grid Origin animated logo + slogan "Create, Organize, Move Forward." |
| **MainDashboardScreen** | `ui/dashboard/MainDashboardScreen.kt` | ❌ Missing | Central hub with counters |
| **ToolLibraryScreen** | `ui/library/ToolLibraryScreen.kt` | ⚠️ Exists under `ui/` | Tree view with hierarchy lines |
| **CommandBuilderScreen** | `app/src/.../ui/commands/CommandBuilderScreen.kt` | ✅ Exists | Functional |
| **AutoModScreen** | `app/src/.../ui/automod/AutoModScreen.kt` | ✅ Exists | Functional |
| **BotCreationScreen** | `app/src/.../ui/launch/BotCreationScreen.kt` | ✅ Exists | Functional |
| **LiveConsoleScreen** | `app/src/.../ui/console/LiveConsoleScreen.kt` | ✅ Exists | Functional |
| **MusicPlayerScreen** | Exists at root | ⚠️ Partial | Needs integration |
| **DoubtAssistantScreen** | `ui/DoubtAssistantScreen.kt` | ✅ Exists | Orion Assistant |
| **StatsDashboardScreen** | ❌ Missing | ❌ Does not exist | Stats/Analytics module |
| **SettingsScreen** | ❌ Missing | ❌ Does not exist | Settings placeholder |

---

## 🎭 LOGO & BRANDING (CRITICAL)

### **Grid Origin Logo**
- **Shape**: Rounded square (20dp radius)
- **Fill**: Blurple `#5865F2`
- **Glyph**: Bold "G" in white, 48sp, SansSerif
- **Size**: 96×96dp logical

### **Loading Animation Ring**
- **Diameter**: 140dp (around 96dp logo)
- **Stroke**: 3dp, round cap, Blurple with opacity sweep
- **Speed**: 1400ms per revolution (linear)
- **Glow**: Alpha oscillates 25%-60% at half rotation period

### **Slogan**
```
"Create, Organize, Advance."
```
- **Font**: 14sp, SansSerif, Normal
- **Color**: TextSecondary `#B5BAC1`

---

## 📂 DIRECTORY STRUCTURE (CORRECT)

```
discord-bot-maker-android/
├── app/src/main/
│   ├── kotlin/com/discordbotmaker/android/
│   │   ├── MainActivity.kt
│   │   ├── ui/
│   │   │   ├── theme/
│   │   │   │   ├── AppTheme.kt ✅ (exists)
│   │   │   │   ├── AppColors.kt (optional - already in AppTheme)
│   │   │   │   └── Typography.kt (optional)
│   │   │   │
│   │   │   ├── navigation/
│   │   │   │   └── AppNavGraph.kt ✅ (this is the official one)
│   │   │   │
│   │   │   ├── splash/
│   │   │   │   ├── SplashScreen.kt ❌
│   │   │   │   └── OriginLoadingScreen.kt ✅
│   │   │   │
│   │   │   ├── dashboard/
│   │   │   │   └── MainDashboardScreen.kt ❌
│   │   │   │
│   │   │   ├── library/
│   │   │   │   └── ToolLibraryScreen.kt ⚠️
│   │   │   │
│   │   │   ├── commands/
│   │   │   │   └── CommandBuilderScreen.kt ✅
│   │   │   │
│   │   │   ├── automod/
│   │   │   │   └── AutoModScreen.kt ✅
│   │   │   │
│   │   │   ├── launch/
│   │   │   │   └── BotCreationScreen.kt ✅
│   │   │   │
│   │   │   ├── console/
│   │   │   │   ├── LiveConsoleScreen.kt ✅
│   │   │   │   └── LiveConsoleViewModel.kt ✅
│   │   │   │
│   │   │   ├── doubt/ (NEW)
│   │   │   │   └── DoubtAssistantScreen.kt ✅
│   │   │   │
│   │   │   ├── stats/ (NEW)
│   │   │   │   └── StatsDashboardScreen.kt ❌
│   │   │   │
│   │   │   ├── music/
│   │   │   │   └── MusicPlayerScreen.kt ⚠️
│   │   │   │
│   │   │   └── components/
│   │   │       ├── OrionBubble.kt
│   │   │       ├── GridBottomNavBar.kt
│   │   │       └── Common composables
│   │   │
│   │   └── models/
│   │       ├── BotCommand.kt
│   │       ├── AutoModConfig.kt
│   │       ├── BotStats.kt (NEW)
│   │       └── etc.
│   │
│   └── res/
│       ├── values/
│       │   ├── strings.xml ❌
│       │   ├── colors.xml ⚠️ (minimal)
│       │   └── themes.xml ❌
│       └── drawable/
│           ├── ic_launcher_background.xml ✅
│           └── ic_launcher_foreground.xml ✅
│
├── backend/
│   └── backend_api.py ✅ (complete FastAPI)
│
├── design/
│   ├── OFFICIAL_MOCKUP.md ✅ (image + guidelines)
│   ├── OFFICIAL_BRANDING.md ✅
│   ├── branding.md ✅ (logo spec)
│   ├── visual-tree.md ✅ (tree structure)
│   └── perspectives.md ✅ (HiFi references)
│
├── build.gradle.kts ✅
├── app/build.gradle.kts ✅
└── settings.gradle.kts ✅
```

---

## ✅ STATUS BY COMPONENT

| Area | Completion | Priority | Blocker |
|------|-------------|-----------|-----------|
| **Backend API** | 100% ✅ | — | NO |
| **Build System** | 100% ✅ | — | NO |
| **Theme (AppTheme.kt)** | 100% ✅ | — | NO |
| **Navigation** | 50% ⚠️ | HIGH | **YES - duplicated** |
| **Logo/Branding** | 80% ⚠️ | MEDIUM | waits for navigation |
| **Dashboard** | 10% ⚠️ | HIGH | **YES - missing** |
| **Tree View** | 30% ⚠️ | HIGH | **YES - incomplete** |
| **Screens (5)** | 60% ⚠️ | MEDIUM | waits for navigation |
| **Resources** | 20% ❌ | MEDIUM | **YES - missing XML** |
| **Tests** | 40% ⚠️ | LOW | — |

---

## 🔴 BLOCKING PROBLEMS

### **1. DUPLICATED NAVIGATION** (CRITICAL)

**Situation:**
- `ui/AppNavigation.kt` (OLD) - with SplashScreen, OriginLoadingScreen
- `app/src/.../ui/navigation/AppNavigation.kt` (NEW) - simplified

**Solution:**
- ✅ Keep ONLY: `app/src/main/kotlin/com/discordbotmaker/android/ui/navigation/AppNavigation.kt`
- ❌ Remove: `ui/AppNavigation.kt`
- Update `MainActivity.kt` to import from the correct path

---

### **2. MISSING SCREENS** (CRITICAL)

```kotlin
❌ SplashScreen.kt
❌ MainDashboardScreen.kt  // the central hub
❌ StatsDashboardScreen.kt
❌ GridBottomNavBar.kt (component)
```

**Correct flow:**
```
MainActivity.kt
  → AppNavGraph() 
    → startDestination = SplashScreen
      → (2-3s delay)
      → OriginLoadingScreen (animated logo)
        → (loading animation completes)
        → MainDashboardScreen (THE HUB)
```

---

### **3. MISSING RESOURCES** (HIGH)

```xml
❌ app/src/main/res/values/strings.xml
❌ app/src/main/res/values/themes.xml
❌ app/src/main/res/values/colors.xml (EXPANDED)
```

**Needed for:**
- externalized strings
- Material 3 theme configuration
- color references from XML

---

## 🚀 ORDERED ACTION PLAN

### **PHASE 1: Structure (1 hour)**
1. ✅ Consolidate navigation (remove `ui/AppNavigation.kt`)
2. ✅ Create the correct directory structure
3. ✅ Create Android resource files

### **PHASE 2: Base Screens (2-3 hours)**
1. `SplashScreen.kt` - simple entry point
2. `MainDashboardScreen.kt` - central hub (WITH counters)
3. `StatsDashboardScreen.kt` - placeholder
4. `GridBottomNavBar.kt` - nav component

### **PHASE 3: Integration (1 hour)**
1. Connect `MainActivity.kt` → `AppNavGraph()`
2. Run `./gradlew :app:assembleDebug`
3. Test on emulator

### **PHASE 4: Refinement (2-3 hours)**
1. Visual details matching the mockup
2. Transition animations
3. Orion floating bubble
4. Pull-to-refresh, etc.

---

## 📊 EXPECTED RESULT

**When PHASE 2 is complete:**
- ✅ App compiles without errors
- ✅ Splash 2-3 sec
- ✅ Animated Grid Origin logo
- ✅ Functional central dashboard
- ✅ Bottom navigation bar
- ✅ Access to 5 main screens

**When PHASE 3 is complete:**
- ✅ **App works on emulator** 📱
- ✅ You can navigate all screens
- ✅ Backend connects for logs/deploy

**When PHASE 4 is complete:**
- ✅ **App ready for Play Store** 🎉

---

## 📖 REQUIRED REFERENCES

1. `design/OFFICIAL_MOCKUP.md` - master mockup (ABSOLUTE TRUTH)
2. `design/branding.md` - exact colors + logo spec
3. `ui/AppTheme.kt` - colors already exist here ✅
4. `COORDINATION.md` - development conventions

---

**Status**: Structurally 100% defined. Implementation 50% complete.
**Next step**: consolidate navigation and create `MainDashboardScreen`.
