# Dashboard design direction

The reference image below remains a visual source for Grid's information hierarchy and brand treatment. It is not a pixel-perfect requirement for every Android screen.

![Grid visual reference](https://customer-assets.emergentagent.com/wingman/85fd1fa5-923c-402f-b2c2-f78866b3e4b5/attachments/5c703d3ae1ea419b82c37f9ef848a102_photo.jpg)

## Bot-specific dashboard

The dashboard represents whichever bot the user creates or selects in the app. Its identity and metrics must come from that bot's data, not from a fixed username or sample values.

Useful dashboard content includes:

- Bot name and online/offline state
- Server and member counts
- Uptime and latency
- Direct access to the live console, AutoMod, commands, and bot launch flow

The user-provided desktop-style dashboard and launch screenshots are additional visual inspiration. Adapt their clear grouping, status visibility, and action hierarchy to the Android interface. They do not define a desktop/web client, which is not part of the current project.

## Current implementation status

`MainDashboardScreen` can render the selected bot values supplied through `BotStatus`. The app does not yet connect bot creation or selection to this state, so the default screen has no selected bot and must not imply that displayed values are live.

Other screens in the mockups may be visual prototypes. Refer to the current source and `README.md` for implementation and backend integration status.
