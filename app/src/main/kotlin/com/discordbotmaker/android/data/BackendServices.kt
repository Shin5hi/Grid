package com.discordbotmaker.android.data

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Backend wiring for the whole app: the saved URL, the REST client and the live log stream.
 * Created once in the navigation graph and shared by every screen.
 */
class BackendServices(context: Context) {

    val config = BackendConfig(context)

    private val http: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val client = BackendClient({ config.baseUrl }, http)

    val logStream = LogStream({ config.baseUrl }, http)

    /** Client for an arbitrary URL, used by Settings to test a value before saving it. */
    fun clientFor(baseUrl: String) = BackendClient({ baseUrl }, http)
}
