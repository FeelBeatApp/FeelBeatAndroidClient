package com.github.feelbeatapp.androidclient.game.datastreaming

import android.util.Log
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.InitialGameState
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.InitialMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.NewPlayerMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.PlayerLeftMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.ServerMessageType
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.infra.error.ErrorCode
import com.github.feelbeatapp.androidclient.infra.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import com.github.feelbeatapp.androidclient.infra.network.NetworkClient
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class RemoteGameDataStreamer
@Inject
constructor(private val networkClient: NetworkClient, private val errorReceiver: ErrorReceiver) :
    GameDataStreamer {
    private var game: Game? = null
    private var gameStateFlow = MutableStateFlow<GameState?>(null)
    private var scope: CoroutineScope? = null

    override suspend fun joinRoom(roomId: String): Deferred<Unit> =
        withContext(Dispatchers.IO) {
            leaveRoom()
            val newScope = CoroutineScope(Dispatchers.IO)
            scope = newScope

            val connectionEstablished = CompletableDeferred<Unit>()

            newScope.launch {
                try {
                    networkClient.connect("/ws/$roomId").collect {
                        if (!connectionEstablished.isCompleted) {
                            connectionEstablished.complete(Unit)
                        }
                        processMessage(it)
                    }
                } catch (e: FeelBeatException) {
                    if (!connectionEstablished.isCompleted) {
                        connectionEstablished.completeExceptionally(e)
                    }
                    errorReceiver.submitError(e)
                } finally {
                    connectionEstablished.completeExceptionally(CancellationException())
                }
            }

            return@withContext connectionEstablished
        }

    override fun leaveRoom() {
        scope?.cancel()
        scope = null
        gameStateFlow.value = null
    }

    override fun gameStateFlow(): StateFlow<GameState?> {
        return gameStateFlow.asStateFlow()
    }

    private fun processMessage(content: String) {
        try {
            val type = Json.decodeFromString<JsonObject>(content)["type"]?.jsonPrimitive?.content

            when (type) {
                ServerMessageType.INITIAL.name ->
                    loadInitialState(Json.decodeFromString<InitialMessage>(content).payload)
                ServerMessageType.NEW_PLAYER.name -> {
                    game?.addPlayer(Json.decodeFromString<NewPlayerMessage>(content).payload)
                    gameStateFlow.value = game?.gameState()
                }
                ServerMessageType.PLAYER_LEFT.name -> {
                    val payload = Json.decodeFromString<PlayerLeftMessage>(content).payload
                    game?.removePlayer(payload.left)
                    game?.setAdmin(payload.admin)
                    gameStateFlow.value = game?.gameState()
                }
                else -> Log.w("RemoteGameDataStreamer", "Received unexpected message: $content")
            }
        } catch (e: Exception) {
            throw FeelBeatException(ErrorCode.FEELBEAT_SERVER_INCORRECT_RESPONSE_FORMAT, e)
        }
    }

    private fun loadInitialState(initialState: InitialGameState) {
        game =
            Game(
                GameState(
                    roomId = initialState.id,
                    playlistName = initialState.playlist.name,
                    playlistImageUrl = initialState.playlist.imageUrl,
                    adminId = initialState.admin,
                    me = initialState.me,
                    players = initialState.players,
                    songs = initialState.playlist.songs.map { it.toSongModel() },
                )
            )
        gameStateFlow.value = game?.gameState()
    }
}
