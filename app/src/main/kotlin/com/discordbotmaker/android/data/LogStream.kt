package com.discordbotmaker.android.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

/** One record from `/ws/logs`, before it is mapped to a console entry. */
data class LogRecord(
    val timestampSeconds: Double,
    val level: String,
    val source: String,
    val message: String
)

/**
 * Live feed from the backend's `/ws/logs` WebSocket.
 *
 * [stream] is meant to run inside a `LaunchedEffect`, so the socket closes when the console
 * leaves the screen and reconnects with backoff when the backend drops.
 */
class LogStream(
    private val baseUrl: () -> String,
    http: OkHttpClient
) {

    // WebSockets must not time out on read; keep-alive pings detect dead peers instead.
    private val socketClient: OkHttpClient = http.newBuilder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .pingInterval(20, TimeUnit.SECONDS)
        .build()

    /**
     * Stays connected until the calling coroutine is cancelled. [onStatus] reports open/closed,
     * [onRecord] receives every log record in arrival order.
     */
    suspend fun stream(onStatus: (Boolean) -> Unit, onRecord: (LogRecord) -> Unit) {
        var backoffMs = MIN_BACKOFF_MS
        while (currentCoroutineContext().isActive) {
            val wasOpen = connectOnce(onStatus, onRecord)
            onStatus(false)
            if (wasOpen) backoffMs = MIN_BACKOFF_MS
            delay(backoffMs)
            backoffMs = (backoffMs * 2).coerceAtMost(MAX_BACKOFF_MS)
        }
    }

    /** Returns whether the socket reached the open state before it closed or failed. */
    private suspend fun connectOnce(
        onStatus: (Boolean) -> Unit,
        onRecord: (LogRecord) -> Unit
    ): Boolean = suspendCancellableCoroutine { continuation ->
        var opened = false
        val request = Request.Builder().url(wsUrl()).build()
        val socket = socketClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                opened = true
                onStatus(true)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                parseRecord(text)?.let(onRecord)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (continuation.isActive) continuation.resume(opened)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                if (continuation.isActive) continuation.resume(opened)
            }
        })
        continuation.invokeOnCancellation { socket.close(NORMAL_CLOSE, "console closed") }
    }

    /** http://host:port -> ws://host:port/ws/logs (https -> wss). */
    private fun wsUrl(): String {
        val base = baseUrl().trimEnd('/')
        val scheme = if (base.startsWith("https://")) "wss://" else "ws://"
        return scheme + base.substringAfter("://") + "/ws/logs"
    }

    private fun parseRecord(text: String): LogRecord? = runCatching {
        val json = JSONObject(text)
        // Other frames (for example pong replies) have no "message" and are ignored.
        if (!json.has("message")) return@runCatching null
        LogRecord(
            timestampSeconds = json.optDouble("timestamp", 0.0),
            level = json.optString("level", "INFO"),
            source = json.optString("source", "bot"),
            message = json.getString("message")
        )
    }.getOrNull()

    private companion object {
        const val MIN_BACKOFF_MS = 1_000L
        const val MAX_BACKOFF_MS = 15_000L
        const val NORMAL_CLOSE = 1000
    }
}
