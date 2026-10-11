package com.discordbotmaker.android.data

import com.discordbotmaker.android.ui.automod.AutoModConfig
import com.discordbotmaker.android.ui.automod.ToxicityAction
import com.discordbotmaker.android.ui.commands.BotCommand
import com.discordbotmaker.android.ui.commands.ResponseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/** A failure reported by the backend: HTTP error, with FastAPI's `detail` when present. */
class BackendException(message: String) : IOException(message)

data class MusicTrackDto(
    val title: String,
    val artist: String,
    val durationSeconds: Int,
    val url: String
)

/** [playbackState] is the backend's `playing` | `paused` | `stopped`. */
data class MusicQueueDto(
    val queue: List<MusicTrackDto>,
    val nowPlaying: MusicTrackDto?,
    val playbackState: String
)

data class DeployResultDto(val deployId: String, val message: String)

private val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()

/**
 * REST client for `backend_api.py`. Every public call runs on IO and returns a [Result],
 * so screens decide how to show failures instead of crashing.
 *
 * [baseUrl] is a provider, not a value, so a URL changed in Settings applies to the next call.
 */
class BackendClient(
    private val baseUrl: () -> String,
    private val http: OkHttpClient
) {

    suspend fun health(): Result<Unit> = call {
        send("GET", "/health")
        Unit
    }

    suspend fun getAutoModConfig(): Result<AutoModConfig> = call {
        parseAutoMod(JSONObject(send("GET", "/api/automod/config")))
    }

    suspend fun putAutoModConfig(config: AutoModConfig): Result<Unit> = call {
        send("PUT", "/api/automod/config", autoModToJson(config))
        Unit
    }

    suspend fun listCommands(): Result<List<BotCommand>> = call {
        val array = JSONArray(send("GET", "/api/commands"))
        List(array.length()) { parseCommand(array.getJSONObject(it)) }
    }

    suspend fun createCommand(command: BotCommand): Result<Unit> = call {
        send("POST", "/api/commands", commandToJson(command))
        Unit
    }

    /** Pushes every command in order. Stops at the first failure. */
    suspend fun saveCommands(commands: List<BotCommand>): Result<Unit> = call {
        commands.forEach { send("POST", "/api/commands", commandToJson(it)) }
        Unit
    }

    suspend fun deleteCommand(name: String): Result<Unit> = call {
        send("DELETE", "/api/commands/$name")
        Unit
    }

    suspend fun musicQueue(): Result<MusicQueueDto> = call {
        val json = JSONObject(send("GET", "/api/music/queue"))
        val queue = json.optJSONArray("queue") ?: JSONArray()
        MusicQueueDto(
            queue = List(queue.length()) { parseTrack(queue.getJSONObject(it)) },
            nowPlaying = json.optJSONObject("now_playing")?.let { parseTrack(it) },
            playbackState = json.optString("playback_state", "stopped")
        )
    }

    /** [action] is one of play, resume, pause, skip, stop. */
    suspend fun musicCommand(action: String): Result<Unit> = call {
        send("POST", "/api/music/play", JSONObject().put("action", action).put("track_url", ""))
        Unit
    }

    suspend fun musicAdd(query: String): Result<Unit> = call {
        send("POST", "/api/music/add", JSONObject().put("query", query))
        Unit
    }

    suspend fun deployBot(token: String, botName: String): Result<DeployResultDto> = call {
        val json = JSONObject(
            send(
                "POST", "/api/bots/deploy",
                JSONObject().put("token", token).put("bot_name", botName)
            )
        )
        DeployResultDto(
            deployId = json.optString("deploy_id"),
            message = json.optString("message")
        )
    }

    private suspend fun <T> call(block: () -> T): Result<T> =
        withContext(Dispatchers.IO) { runCatching(block) }

    private fun send(method: String, path: String, body: JSONObject? = null): String {
        // OkHttp requires a body for POST/PUT; GET and DELETE can go without one.
        val requestBody: RequestBody? = when {
            body != null -> body.toString().toRequestBody(JSON_MEDIA)
            method == "POST" || method == "PUT" -> "".toRequestBody(JSON_MEDIA)
            else -> null
        }
        val url = baseUrl().trimEnd('/') + path
        val request = Request.Builder().url(url).method(method, requestBody).build()
        http.newCall(request).execute().use { response ->
            val text = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw BackendException(errorMessage(response.code, text))
            return text
        }
    }

    private fun errorMessage(code: Int, text: String): String {
        val detail = runCatching { JSONObject(text).opt("detail") }.getOrNull()
        return when (detail) {
            is String -> detail
            null -> "HTTP $code"
            else -> "HTTP $code: request rejected by the backend"
        }
    }

    private fun parseAutoMod(json: JSONObject) = AutoModConfig(
        toxicityFilterEnabled = json.optBoolean("toxicity_filter_enabled"),
        toxicitySensitivity = json.optDouble("toxicity_sensitivity", 0.7).toFloat(),
        toxicityAction = runCatching {
            ToxicityAction.valueOf(json.optString("toxicity_action", "DELETE"))
        }.getOrDefault(ToxicityAction.DELETE),
        linkBlockingEnabled = json.optBoolean("link_blocking_enabled"),
        allowWhitelistedLinks = json.optBoolean("allow_whitelisted_links", true),
        spamProtectionEnabled = json.optBoolean("spam_protection_enabled"),
        spamMessageThreshold = json.optInt("spam_message_threshold", 5),
        spamWindowSeconds = json.optInt("spam_window_seconds", 10),
        spamMuteDurationMinutes = json.optInt("spam_mute_duration_minutes", 5)
    )

    private fun autoModToJson(config: AutoModConfig) = JSONObject()
        .put("toxicity_filter_enabled", config.toxicityFilterEnabled)
        .put("toxicity_sensitivity", config.toxicitySensitivity.toDouble())
        .put("toxicity_action", config.toxicityAction.name)
        .put("link_blocking_enabled", config.linkBlockingEnabled)
        .put("allow_whitelisted_links", config.allowWhitelistedLinks)
        .put("spam_protection_enabled", config.spamProtectionEnabled)
        .put("spam_message_threshold", config.spamMessageThreshold)
        .put("spam_window_seconds", config.spamWindowSeconds)
        .put("spam_mute_duration_minutes", config.spamMuteDurationMinutes)

    private fun parseCommand(json: JSONObject) = BotCommand(
        name = json.getString("name"),
        responseType = runCatching {
            ResponseType.valueOf(json.optString("response_type", "TEXT"))
        }.getOrDefault(ResponseType.TEXT),
        responseContent = json.optString("response_content")
    )

    private fun commandToJson(command: BotCommand) = JSONObject()
        .put("name", command.name)
        .put("response_type", command.responseType.name)
        .put("response_content", command.responseContent)

    private fun parseTrack(json: JSONObject) = MusicTrackDto(
        title = json.optString("title"),
        artist = json.optString("artist"),
        durationSeconds = json.optInt("duration_seconds"),
        url = json.optString("url")
    )
}
