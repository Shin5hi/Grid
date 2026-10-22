# Grid repo cleanup script

This repo contains a number of stale validation artifacts, temporary files, and legacy documents that should not stay in the canonical project tree.

To run:

```bash
chmod +x scripts/cleanup_repo.sh
./scripts/cleanup_repo.sh
```

What it removes:
- `test_*.py` files created for validation only
- `*.tmp` / `*.tmp1` temporary files
- legacy docs such as `MERGE_PR_5_STATUS.md`, `resumen_proyecto_bot.md`, and `CONSOLIDACION_COMPLETADA.md`
- empty placeholder backend artifact under `backend/backend_api.py`

This keeps the app structure aligned with the active Kotlin Android project while preserving the real backend files.
