#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

# Remove orphaned validation artifacts and temp files
find "$repo_root" -maxdepth 1 -type f -name 'test_*.py' -delete
find "$repo_root" -maxdepth 2 -type f -name '*.tmp' -delete
find "$repo_root" -maxdepth 2 -type f -name '*.tmp1' -delete

# Remove stale legacy docs that are no longer part of the canonical structure
rm -f \
  "$repo_root/MERGE_PR_5_STATUS.md" \
  "$repo_root/resumen_proyecto_bot.md" \
  "$repo_root/CONSOLIDACION_COMPLETADA.md"

# Remove placeholder backend artifact only if the file is still empty/placeholder
if [ -f "$repo_root/backend/backend_api.py" ] && [ ! -s "$repo_root/backend/backend_api.py" ]; then
  rm -f "$repo_root/backend/backend_api.py"
fi

# If backend folder becomes empty, remove it
if [ -d "$repo_root/backend" ] && [ -z "$(ls -A "$repo_root/backend" 2>/dev/null)" ]; then
  rmdir "$repo_root/backend" 2>/dev/null || true
fi

echo "Repository cleanup script executed successfully."
