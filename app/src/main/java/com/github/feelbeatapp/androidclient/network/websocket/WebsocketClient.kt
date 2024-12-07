package com.github.feelbeatapp.androidclient.network.websocket

import android.util.Log
import com.github.feelbeatapp.androidclient.network.NetworkAgent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import java.net.ConnectException
import java.util.ArrayDeque
import java.util.Queue
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/** Websocket implementation of communication with FeelBeat server */
class WebsocketClient @Inject constructor(private val httpClient: HttpClient, private val serverUrl: Url) :
    NetworkAgent {
    companion object {
        private const val RECONNECT_DELAY_MS = 500
    }

    private var session: WebSocketSession? = null
    private val receiveFlow = MutableSharedFlow<String>()
    private val offlineQueue: Queue<String> = ArrayDeque()

    private suspend fun receiveWorker(incomingChannel: ReceiveChannel<Frame>) {
        incomingChannel.consumeAsFlow().collect { frame ->
            when (frame) {
                is Frame.Text -> receiveFlow.emit(frame.readText())
                else -> Log.d("unidentified message", frame.data.toString())
            }
        }
    }

    override fun connect(path: String) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    httpClient.webSocket(
                        method = HttpMethod.Get,
                        host = serverUrl.host,
                        port = serverUrl.port,
                        path = "${serverUrl.fullPath}${path}",
                    ) {
                        session = this
                        runBlocking {
                            launch { receiveWorker(checkNotNull(session).incoming) }

                            // Send stacked messages
                            launch {
                                for (msg in offlineQueue) {
                                    checkNotNull(session).send(msg)
                                }
                                offlineQueue.clear()
                            }
                        }
                        session = null
                    }
                } catch (_: ConnectException) {
                    Log.e("websocket", "Connection exception occurred")
                    delay(RECONNECT_DELAY_MS.milliseconds)
                }
            }
        }
    }

    override suspend fun disconnect() {
        session?.close()
    }

    override suspend fun sendMessage(text: String) {
        if (session == null) {
            Log.d("websocket", "Watch stacking $text")
            offlineQueue.offer(text)
        } else {
            session?.send(text)
        }
    }

    override fun receiveFlow(): SharedFlow<String> {
        return receiveFlow
    }
}
