package com.github.feelbeatapp.androidclient.websocket

import android.util.Log
import com.github.feelbeatapp.androidclient.network.NetworkAgent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WebsocketClient @Inject constructor() : NetworkAgent {
    private val httpClient = HttpClient(CIO) { install(WebSockets) }
    var isConnected: Boolean = false
        private set

    private val outgoingPipe = MutableSharedFlow<String>()
    private val incomingPipe = MutableSharedFlow<String>()
    private var job: Job? = null

    private suspend fun readLoop(session: WebSocketSession) {
        for (frame in session.incoming) {
            when (frame) {
                is Frame.Text -> incomingPipe.emit(frame.readText())
                else -> Log.d("WebsocketClient", "Received: ${frame.data}")
            }
        }
    }

    private suspend fun writeLoop(session: WebSocketSession) {
        outgoingPipe.collect { session.send(it) }
    }

    fun connect(host: String, port: Int, path: String) {
        job =
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    httpClient.webSocket(
                        method = HttpMethod.Get,
                        host = host,
                        port = port,
                        path = path,
                    ) {
                        val session = this
                        runBlocking {
                            launch { readLoop(session) }
                            launch { writeLoop(session) }
                        }
                    }
                } catch (e: WebSocketException) {
                    Log.e("WebsocketClient", "Connection failed: ${e.message}")
                }
            }
    }

    override suspend fun sendMessage(text: String) {
        outgoingPipe.emit(text)
    }

    override fun incomingFlow(): SharedFlow<String> {
        return incomingPipe
    }
}
