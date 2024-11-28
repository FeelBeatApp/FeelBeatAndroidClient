package com.github.feelbeatapp.androidclient.websocket

import android.util.Log
import com.github.feelbeatapp.androidclient.network.NetworkAgent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// TODO: Implement stacking messages when offline, simplify whole implementation

/** Websocket implementation of communication with FeelBeat server */
class WebsocketClient(private val httpClient: HttpClient) : NetworkAgent {
    private var session: WebSocketSession? = null
    private val receiveFlow = MutableSharedFlow<String>()

    private suspend fun receiveWorker(incomingChannel: ReceiveChannel<Frame>) {
        incomingChannel.consumeAsFlow().collect { frame ->
            when (frame) {
                is Frame.Text -> receiveFlow.emit(frame.readText())
                else -> Log.d("unidentified message", frame.data.toString())
            }
        }
    }

    fun connect(host: String, port: Int, path: String) {
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.webSocket(method = HttpMethod.Get, host = host, port = port, path = path) {
                val tmp = this // avoid being nullable when passing to receiveWorker
                session = tmp
                runBlocking { receiveWorker(tmp.incoming) }
            }
        }
    }

    suspend fun disconnect() {
        session?.close()
    }

    override suspend fun sendMessage(text: String) {
        session?.send(text)
    }

    override fun receiveFlow(): SharedFlow<String> {
        return receiveFlow
    }
}
