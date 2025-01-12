package com.github.feelbeatapp.androidclient.infra.network

import android.util.Log
import com.github.feelbeatapp.androidclient.infra.auth.AuthManager
import com.github.feelbeatapp.androidclient.infra.error.ErrorCode
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import java.util.ArrayDeque
import java.util.Queue
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/** Websocket implementation of communication with FeelBeat server */
class WebsocketClient
@Inject
constructor(
    private val httpClient: HttpClient,
    @Named("SOCKET_URI") private val serverUrl: Url,
    private val authManager: AuthManager,
) : NetworkClient {
    private var session: WebSocketSession? = null
    private val offlineQueue: Queue<String> = ArrayDeque()

    override suspend fun connect(path: String): Flow<String> {
        return flow {
                val token = authManager.getAccessToken()

                httpClient.webSocket(
                    method = HttpMethod.Get,
                    host = serverUrl.host,
                    port = serverUrl.port,
                    path = "${serverUrl.fullPath}${path}",
                    request = { header("Authorization", "Bearer $token") },
                ) {
                    for (msg in offlineQueue) {
                        this.send(msg)
                    }
                    offlineQueue.clear()

                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            emit(frame.readText())
                        } else {
                            Log.w("Ignoring message", frame.data.decodeToString())
                        }
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .catch { e ->
                throw FeelBeatException(ErrorCode.FEELBEAT_SERVER_FAILED_TO_JOIN_ROOM, e)
            }
    }

    override suspend fun disconnect() {
        session?.close()
    }

    override suspend fun sendMessage(text: String) {
        if (session == null) {
            offlineQueue.offer(text)
        } else {
            session?.send(text)
        }
    }
}
