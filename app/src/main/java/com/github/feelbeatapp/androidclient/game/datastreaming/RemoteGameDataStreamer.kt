package com.github.feelbeatapp.androidclient.game.datastreaming

import android.util.Log
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.InitialGameState
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.InitialMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.NewPlayerMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.PlayerLeftMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.ServerMessageType
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.infra.error.ErrorCode
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import com.github.feelbeatapp.androidclient.infra.network.NetworkClient
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class RemoteGameDataStreamer @Inject constructor(private val networkClient: NetworkClient) :
    GameDataStreamer {
    private var game: Game? = null
    private var gameStateFlow = MutableStateFlow<GameState?>(null)
    private var scope: CoroutineScope? = null

    override suspend fun joinRoom(roomId: String) {
        scope?.cancel()
        val newScope = CoroutineScope(Dispatchers.IO)
        scope = newScope
        newScope.launch { networkClient.connect("/ws/$roomId").collect { processMessage(it) } }
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
                    game?.removePlayer(Json.decodeFromString<PlayerLeftMessage>(content).payload)
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
                    players = initialState.players,
                    songs = initialState.playlist.songs.map { it.toSongModel() },
                )
            )
        gameStateFlow.value = game?.gameState()
    }
}
