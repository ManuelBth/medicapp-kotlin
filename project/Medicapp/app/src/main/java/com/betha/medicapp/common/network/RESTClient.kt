package com.betha.medicapp.common.network

import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*

class RESTClient(private val server: String) {
    private var currentDeferred: CompletableDeferred<String>? = null

    fun httpPostAsync(url: String, data: String) {
        currentDeferred = CompletableDeferred()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val conn = URL(server + url).openConnection() as HttpURLConnection
                try {
                    conn.apply {
                        requestMethod = "POST"
                        doInput = true
                        doOutput = true
                        setRequestProperty("Content-Type", "application/json")
                        readTimeout = 10000
                        connectTimeout = 15000
                    }
                    conn.outputStream.use { it.write(data.toByteArray()) }

                    if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                        throw Exception("HTTP error ${conn.responseCode}")
                    }
                    val response = conn.inputStream.bufferedReader().readText()
                    currentDeferred?.complete(response)
                } finally {
                    conn.disconnect()
                }
            } catch (e: Exception) {
                currentDeferred?.completeExceptionally(e)
            }
        }
    }

    fun httpGetAsync(url: String) {
        currentDeferred = CompletableDeferred()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val conn = URL(server + url).openConnection() as HttpURLConnection
                try {
                    conn.apply {
                        requestMethod = "GET"
                        connectTimeout = 5000
                        readTimeout = 5000
                    }
                    if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                        throw Exception("HTTP error ${conn.responseCode}")
                    }
                    val response = conn.inputStream.bufferedReader().readText()
                    currentDeferred?.complete(response)
                } finally {
                    conn.disconnect()
                }
            } catch (e: Exception) {
                currentDeferred?.completeExceptionally(e)
            }
        }
    }

    suspend fun wait(): String {
        return currentDeferred?.await() ?: throw IllegalStateException("No request in progress")
    }
}