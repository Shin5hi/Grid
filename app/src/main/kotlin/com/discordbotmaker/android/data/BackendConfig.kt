package com.discordbotmaker.android.data

import android.content.Context

/**
 * Persists the backend base URL chosen in Settings.
 *
 * The default points at the Android emulator's host loopback, where `uvicorn backend_api:app
 * --host 0.0.0.0 --port 8000` is reachable. On a physical device, set the PC's LAN address.
 */
class BackendConfig(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var baseUrl: String
        get() = prefs.getString(KEY_BASE_URL, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
        set(value) {
            prefs.edit().putString(KEY_BASE_URL, normalize(value)).apply()
        }

    companion object {
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8000"
        private const val PREFS_NAME = "backend"
        private const val KEY_BASE_URL = "base_url"

        /** True when [raw] is an absolute http(s) URL, the only schemes the client supports. */
        fun isValidUrl(raw: String): Boolean {
            val url = raw.trim()
            return (url.startsWith("http://") || url.startsWith("https://")) &&
                url.substringAfter("://").isNotBlank()
        }

        /** Trims whitespace and trailing slashes; falls back to the default when empty. */
        fun normalize(raw: String): String =
            raw.trim().trimEnd('/').ifEmpty { DEFAULT_BASE_URL }
    }
}
