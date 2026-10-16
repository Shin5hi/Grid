"""
Validation tests for the FastAPI backend (backend_api.py).

Static structural checks always run; a live test that imports the app runs
only when FastAPI is installed (otherwise it is skipped so the suite stays
green on machines without the backend dependencies).
"""
import ast
import importlib.util
import os

import pytest

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))


def _backend_source() -> str:
    with open(os.path.join(PROJECT_ROOT, "backend_api.py"), "r", encoding="utf-8") as f:
        return f.read()


def _parse() -> ast.Module:
    return ast.parse(_backend_source())


fastapi_installed = importlib.util.find_spec("fastapi") is not None


class TestBackendStructure:
    """backend_api.py must parse and expose the expected FastAPI app."""

    def test_parses(self):
        # ast.parse raises SyntaxError on malformed files.
        _parse()

    def test_defines_app(self):
        tree = _parse()
        names = {
            t.id
            for node in ast.walk(tree)
            if isinstance(node, ast.Assign)
            for t in node.targets
            if isinstance(t, ast.Name)
        }
        assert "app" in names, "FastAPI 'app' instance not defined"

    def test_toxicity_action_has_ban_not_bam(self):
        content = _backend_source()
        assert "BAN" in content
        assert "BAM" not in content, "Found 'BAM' typo (should be BAN)"

    def test_expected_routes_present(self):
        tree = _parse()
        routes = set()
        for n in ast.walk(tree):
            if (
                isinstance(n, ast.Call)
                and isinstance(n.func, ast.Attribute)
                and n.func.attr in ("websocket", "get", "post", "delete")
                and n.args
                and isinstance(n.args[0], ast.Constant)
            ):
                routes.add(n.args[0].value)
        assert "/ws/logs" in routes, "Log stream WebSocket /ws/logs missing"
        assert "/api/bots/deploy" in routes, "Deploy endpoint missing"
        assert "/api/commands" in routes, "Commands endpoints missing"


@pytest.mark.skipif(not fastapi_installed, reason="fastapi not installed")
class TestBackendLive:
    """Live tests requiring FastAPI. Skipped when deps are absent."""

    def test_app_imports(self):
        from backend_api import app  # noqa: F401

        assert app.title == "Discord Bot Maker API"
