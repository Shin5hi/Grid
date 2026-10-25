# Repository maintenance script

`cleanup_repo.sh` is a destructive cleanup utility, not a routine setup or validation step. Review its contents and preserve any files you need before running it.

From the repository root on macOS or Linux:

```bash
./scripts/cleanup_repo.sh
```

The script deletes root-level `test_*.py` files, root-level `*.tmp` and `*.tmp1` files, and the named legacy documents `MERGE_PR_5_STATUS.md`, `resumen_proyecto_bot.md`, and `CONSOLIDACION_COMPLETADA.md`. It removes `backend/backend_api.py` only if the file is empty, and removes the `backend/` directory only if it is empty afterward.

It does not remove the root `backend_api.py` module or the Android app.
