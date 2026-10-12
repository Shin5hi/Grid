"""Tests for GET /api/status and POST /api/assistant.

Redis is replaced by fakeredis and the assistant provider by an httpx.MockTransport,
so these tests make no network calls.
"""
import json
import os
import sys

import fakeredis
import httpx
import pytest
from fastapi.testclient import TestClient

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

import backend_api  # noqa: E402


@pytest.fixture
def client(monkeypatch):
    fake = fakeredis.aioredis.FakeRedis(decode_responses=True)
    monkeypatch.setattr(backend_api, "redis_pool", fake)
    for var in ("ASSISTANT_PROVIDER", "ASSISTANT_MODEL", "ANTHROPIC_API_KEY", "OPENAI_API_KEY"):
        monkeypatch.delenv(var, raising=False)
    monkeypatch.setattr(backend_api, "ASSISTANT_TRANSPORT", None)
    with TestClient(backend_api.app) as c:
        c.fake_redis = fake
        yield c


# ─── /api/status ─────────────────────────────────────────────────────────────

def test_status_empty_store(client):
    body = client.get("/api/status").json()
    assert body == {
        "redis": "connected",
        "commands": 0,
        "deployments": 0,
        "music": {"playback_state": "stopped", "queue_length": 0},
        "bot_runtime": "not_connected",
    }


def test_status_counts_real_data(client):
    import asyncio

    async def seed():
        r = client.fake_redis
        await r.hset(backend_api.COMMANDS_HASH_KEY, "ping", "{}")
        await r.hset(backend_api.COMMANDS_HASH_KEY, "help", "{}")
        await r.set(f"{backend_api.DEPLOY_STATE_KEY}:abc", "{}")
        await r.set(backend_api.MUSIC_QUEUE_KEY, json.dumps([{"title": "a"}, {"title": "b"}, {"title": "c"}]))
        await r.set(backend_api.MUSIC_PLAYBACK_KEY, "paused")

    asyncio.run(seed())
    body = client.get("/api/status").json()
    assert body["commands"] == 2
    assert body["deployments"] == 1
    assert body["music"] == {"playback_state": "paused", "queue_length": 3}


def test_status_reports_broken_queue_as_empty(client):
    import asyncio

    asyncio.run(client.fake_redis.set(backend_api.MUSIC_QUEUE_KEY, "not json"))
    assert client.get("/api/status").json()["music"]["queue_length"] == 0


# ─── /api/assistant ──────────────────────────────────────────────────────────

def test_assistant_unconfigured_returns_503(client):
    resp = client.post("/api/assistant", json={"message": "hola"})
    assert resp.status_code == 503
    assert "not configured" in resp.json()["detail"]


def test_assistant_rejects_empty_message(client, monkeypatch):
    monkeypatch.setenv("ASSISTANT_PROVIDER", "anthropic")
    monkeypatch.setenv("ANTHROPIC_API_KEY", "test-key")
    monkeypatch.setenv("ASSISTANT_MODEL", "test-model")
    assert client.post("/api/assistant", json={"message": ""}).status_code == 422


def test_assistant_anthropic_reply(client, monkeypatch):
    seen = {}

    def handler(request: httpx.Request) -> httpx.Response:
        seen["url"] = str(request.url)
        seen["key"] = request.headers.get("x-api-key")
        seen["body"] = json.loads(request.content)
        return httpx.Response(200, json={"content": [{"type": "text", "text": "Usa /help."}]})

    monkeypatch.setenv("ASSISTANT_PROVIDER", "anthropic")
    monkeypatch.setenv("ANTHROPIC_API_KEY", "test-key")
    monkeypatch.setenv("ASSISTANT_MODEL", "test-model")
    monkeypatch.setattr(backend_api, "ASSISTANT_TRANSPORT", httpx.MockTransport(handler))

    resp = client.post("/api/assistant", json={"message": "¿cómo añado un comando?"})
    assert resp.status_code == 200
    assert resp.json() == {"reply": "Usa /help.", "provider": "anthropic"}
    assert seen["url"] == "https://api.anthropic.com/v1/messages"
    assert seen["key"] == "test-key"
    assert seen["body"]["model"] == "test-model"
    assert seen["body"]["messages"][0]["content"] == "¿cómo añado un comando?"


def test_assistant_openai_reply(client, monkeypatch):
    def handler(request: httpx.Request) -> httpx.Response:
        assert request.headers["authorization"] == "Bearer oa-key"
        return httpx.Response(200, json={"choices": [{"message": {"content": "Listo."}}]})

    monkeypatch.setenv("ASSISTANT_PROVIDER", "openai")
    monkeypatch.setenv("OPENAI_API_KEY", "oa-key")
    monkeypatch.setenv("ASSISTANT_MODEL", "test-model")
    monkeypatch.setattr(backend_api, "ASSISTANT_TRANSPORT", httpx.MockTransport(handler))

    assert client.post("/api/assistant", json={"message": "hola"}).json()["reply"] == "Listo."


def test_assistant_provider_error_returns_502(client, monkeypatch):
    def handler(request: httpx.Request) -> httpx.Response:
        return httpx.Response(500, json={"error": "boom"})

    monkeypatch.setenv("ASSISTANT_PROVIDER", "anthropic")
    monkeypatch.setenv("ANTHROPIC_API_KEY", "test-key")
    monkeypatch.setenv("ASSISTANT_MODEL", "test-model")
    monkeypatch.setattr(backend_api, "ASSISTANT_TRANSPORT", httpx.MockTransport(handler))

    resp = client.post("/api/assistant", json={"message": "hola"})
    assert resp.status_code == 502
