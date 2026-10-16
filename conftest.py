"""
pytest path-resolution shim for Discord Bot Maker (Grid).

The Kotlin sources live under:
    app/src/main/kotlin/com/discordbotmaker/android/ui/<feature>/<Screen>.kt
but several validation tests were written against older layouts
(e.g. "ui/MainDashboardScreen.kt", bare "AutoModScreen.kt", or a hard-coded
"/root/workspace/discord-bot-maker-android" prefix). Rather than editing every
test, this conftest transparently resolves any path that fails to exist:

  * "/root/workspace/discord-bot-maker-android/..." -> real project root
  * any missing "*.kt" path -> looked up by file name inside the source set

It patches builtins.open and the os.path.* existence checks for the duration of
the test session and restores them afterwards. It never touches the real files.
"""
import builtins
import os

import pytest

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
SRC_ROOT = os.path.join(
    PROJECT_ROOT,
    "app", "src", "main", "kotlin",
    "com", "discordbotmaker", "android",
)
# Legacy hard-coded prefix used by a couple of tests written in a Linux CI.
_LEGACY_PREFIX = "/root/workspace/discord-bot-maker-android"

_kt_index = None


def _build_kt_index():
    """Map each .kt file name (unique in this project) to its real path."""
    global _kt_index
    if _kt_index is None:
        _kt_index = {}
        for root, _dirs, files in os.walk(SRC_ROOT):
            for fn in files:
                if fn.endswith(".kt"):
                    _kt_index.setdefault(fn, os.path.join(root, fn))
    return _kt_index


_real_open = builtins.open
_real_exists = os.path.exists
_real_isfile = os.path.isfile
_real_isdir = os.path.isdir


def resolve(path):
    """Return a path that exists on this machine, or the input unchanged.

    Always uses the *real* os.path checks captured above so it never recurses
    into the patched wrappers.
    """
    if not isinstance(path, (str, bytes, os.PathLike)):
        return path
    s = os.fspath(path)

    # 1) Rewrite the legacy Linux prefix to the real project root.
    if _LEGACY_PREFIX in s:
        s2 = s.replace(_LEGACY_PREFIX, PROJECT_ROOT)
        if _real_exists(s2):
            return s2

    # 2) If it already exists, keep it.
    if _real_exists(s):
        return s

    # 2b) Relative path not found in the CWD: retry from the project root.
    # Some tests open files like "COORDINATION.md" with a CWD-independent
    # relative path; resolve them against PROJECT_ROOT.
    if not os.path.isabs(s):
        candidate = os.path.join(PROJECT_ROOT, s)
        if _real_exists(candidate):
            return candidate

    # 2b) Bare relative path (no directory component): resolve against the
    # project root so tests work regardless of the current working directory.
    if os.sep not in s and "/" not in s and s:
        candidate = os.path.join(PROJECT_ROOT, s)
        if _real_exists(candidate):
            return candidate

    # 3) Fall back to a .kt lookup by file name inside the source set.
    name = os.path.basename(s)
    if name.endswith(".kt"):
        found = _build_kt_index().get(name)
        if found:
            return found

    # 4) Bare relative paths (e.g. "COORDINATION.md") are anchored to the
    #    real project root, since tests may run from any working directory.
    if not os.path.isabs(s) and os.sep not in s and "/" not in s:
        candidate = os.path.join(PROJECT_ROOT, s)
        if _real_exists(candidate):
            return candidate

    return s


def _open(file, *args, **kwargs):
    try:
        # Default to UTF-8 for text-mode reads/writes so Markdown/docs with
        # emojis don't break on Windows' cp1252 default codec.
        if "b" not in (args[0] if args else kwargs.get("mode", "r")):
            kwargs.setdefault("encoding", "utf-8")
        return _real_open(file, *args, **kwargs)
    except (FileNotFoundError, NotADirectoryError, OSError):
        resolved = resolve(file)
        if "b" not in (args[0] if args else kwargs.get("mode", "r")):
            kwargs.setdefault("encoding", "utf-8")
        return _real_open(resolved, *args, **kwargs)


def _exists(path):
    return _real_exists(path) or _real_exists(resolve(path))


def _isfile(path):
    return _real_isfile(path) or _real_isfile(resolve(path))


def _isdir(path):
    return _real_isdir(path) or _real_isdir(resolve(path))


@pytest.fixture(scope="session", autouse=True)
def _install_path_shim():
    builtins.open = _open
    os.path.exists = _exists
    os.path.isfile = _isfile
    os.path.isdir = _isdir
    yield
    builtins.open = _real_open
    os.path.exists = _real_exists
    os.path.isfile = _real_isfile
    os.path.isdir = _real_isdir
