# 🤝 Grid & Codex Collaboration Board

## ⚠️ ORDEN DIRECTA: 100% FIDELIDAD VISUAL
El jefe ha aprobado el Mockup Maestro definitivo. 

**Codex / Sprite / Gaul:** 
Queda prohibido cualquier cambio estético que no esté presente en `design/OFFICIAL_MOCKUP.md`. 
- El backend debe soportar la densidad de datos mostrada (Proyectos, Tareas, Herramientas, Notas).
- El frontend (Android) debe ser un clon de esta imagen.

¡A trabajar! 🚀

---

## Migration Status

La migración del frontend Android está en curso. Estado actual:

- **pending / waiting**: la pantalla de Billing sigue pendiente de implementación (placeholder).
- Permisos por organización (org permissions): cada servidor pertenece a una organización y sus
  permisos se resuelven por rol. Pendiente conectar el backend de permisos por org.
- Fork personal del autor: `Shin5hi/discord-bot-maker-android`.

## Server Tree View

La pantalla de herramientas usa un **Tree View** jerárquico tipo servidor de Discord:

- Categorías colapsables (compact / high-density) con ramas (branch lines) dibujadas mediante
  `Canvas` + `drawLine` para conectar padres e hijos.
- Tipos de datos `TreeChannel` y `TreeCategory` en `ui/library/ToolLibraryScreen.kt`.
- Layout de alta densidad (high-density) para que quepan muchos canales por pantalla.

## Asistente Orión (OrionBubble FAB)

- `ui/components/OrionBubble.kt`: botón flotante (FAB) arrastrable (`pointerInput` +
  `detectDragGestures`) con antenas, ojos y animación de pulso (`rememberInfiniteTransition`).
- Integrado en el `Scaffold` de `ui/navigation/AppNavigation.kt`; navega al Asistente Orión
  (`DoubtAssistantScreen`).
- Se oculta en la ruta `SPLASH` (y en la propia pantalla de Orión) vía el set `hideOrionRoutes`
  y la bandera `showOrionBubble`.

## Stats Dashboard Module

Módulo de métricas del bot.

- **Archivo (ownership, estado Active):** `ui/StatsDashboardScreen.kt`
- **Ruta:** `stats_dashboard`
- **Modelos:** `BotStats`, `ActivityDataPoint`
- **Componentes:** `StatsCardGrid` / `StatCard`, `ActivityChart`
- Cumple la dirección de arte: tarjetas planas con elevación `0.dp` y esquinas `8.dp`.

## Art Direction

- Blurple primario: `#5865F2`
- Fondo charcoal: `#313338`
- Superficie: `#2B2D31`
- Tarjetas planas: elevación `0.dp`, esquinas `8.dp`.

## Conventions

- Todos los colores se referencian vía `AppColors.*`; nunca colores hexadecimales inline en pantallas.
- Tipografía por defecto de Material (nada de `FontFamily.Monospace`).
- Código Kotlin en el source set `app/src/main/kotlin/...` con un paquete por pantalla.

## File Ownership

| Archivo | Owner | Estado |
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
