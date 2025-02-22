package com.github.feelbeatapp.androidclient.game.datastreaming

import android.util.Log
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.client.GuessSongMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.client.GuessSongPayload
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.client.ReadyStatusMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.client.SettingsUpdateMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.client.SettingsUpdatePayload
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.CorrectSongMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.EndGameMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.InitialGameState
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.InitialMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.NewPlayerMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.PlaySongMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.PlayerGuessMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.PlayerLeftMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.PlayerReadyMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.RoomStageMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.ServerErrorMessage
import com.github.feelbeatapp.androidclient.game.datastreaming.messages.server.ServerMessageType
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.GuessCorrectness
import com.github.feelbeatapp.androidclient.game.model.PlayerFinalScore
import com.github.feelbeatapp.androidclient.game.model.RoomSettings
import com.github.feelbeatapp.androidclient.game.model.RoomStage
import com.github.feelbeatapp.androidclient.infra.auth.AuthManager
import com.github.feelbeatapp.androidclient.infra.error.ErrorCode
import com.github.feelbeatapp.androidclient.infra.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatServerException
import com.github.feelbeatapp.androidclient.infra.network.NetworkClient
import java.time.Duration
import java.time.Instant
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class RemoteGameDataStreamer
@Inject
constructor(
    private val networkClient: NetworkClient,
    private val errorReceiver: ErrorReceiver,
    private val authManager: AuthManager,
) : GameDataStreamer {
    private var game: Game? = null
    private var gameStateFlow = MutableStateFlow<GameState?>(null)
    private var resultStateFlow = MutableStateFlow<List<PlayerFinalScore>>(listOf())
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
        synchronized(this) {
            game = null
            gameStateFlow.value = null
        }
    }

    override fun gameStateFlow(): StateFlow<GameState?> {
        return gameStateFlow.asStateFlow()
    }

    override fun lastGameResultStateFlow(): StateFlow<List<PlayerFinalScore>> {
        return resultStateFlow.asStateFlow()
    }

    override suspend fun updateSettings(settings: RoomSettings) {
        withContext(Dispatchers.IO) {
            val token = authManager.getAccessToken()
            val message =
                SettingsUpdateMessage(
                    payload = SettingsUpdatePayload(token = token, settings = settings)
                )
            networkClient.sendMessage(Json.encodeToString(message))
        }
    }

    override suspend fun sendReadyStatus(ready: Boolean) {
        synchronized(this) {
            game.let {
                if (it == null) {
                    return
                }

                game
                it.setMyReadyStatus(ready)
                gameStateFlow.value = it.gameState()
            }
        }

        resultStateFlow.value = listOf()

        withContext(Dispatchers.IO) {
            networkClient.sendMessage(Json.encodeToString(ReadyStatusMessage(payload = ready)))
        }
    }

    override suspend fun sendGuess(id: String, points: Int) {
        synchronized(this) {
            game.let {
                if (it == null) {
                    return
                }
                if (
                    it.gameState().songGuessMap.getOrDefault(id, GuessCorrectness.UNKNOWN) !=
                        GuessCorrectness.UNKNOWN
                ) {
                    return
                }

                it.markGuess(id)

                if (points == 0) {
                    it.setPlayerGuessResult(it.gameState().me ?: "", false)
                }

                gameStateFlow.value = it.gameState()
            }
        }

        withContext(Dispatchers.IO) {
            networkClient.sendMessage(
                Json.encodeToString(
                    GuessSongMessage(payload = GuessSongPayload(id = id, points = points))
                )
            )
        }
    }

    private suspend fun processMessage(content: String) {
        try {
            val type = Json.decodeFromString<JsonObject>(content)["type"]?.jsonPrimitive?.content

            when (type) {
                ServerMessageType.SERVER_ERROR.name -> processServerError(content)
                ServerMessageType.INITIAL.name ->
                    loadInitialState(Json.decodeFromString<InitialMessage>(content).payload)
                ServerMessageType.NEW_PLAYER.name -> processNewPlayer(content)
                ServerMessageType.PLAYER_LEFT.name -> processPlayerLeft(content)
                ServerMessageType.PLAYER_READY.name -> processPlayerReady(content)
                ServerMessageType.ROOM_STAGE.name -> processRoomStage(content)
                ServerMessageType.PLAY_SONG.name -> processPlaySong(content)
                ServerMessageType.PLAYER_GUESS.name -> processPlayerGuess(content)
                ServerMessageType.CORRECT_SONG.name -> processCorrectSong(content)
                ServerMessageType.END_GAME.name -> processEndGame(content)
                else -> Log.w("RemoteGameDataStreamer", "Received unexpected message: $content")
            }
        } catch (e: Exception) {
            throw FeelBeatException(ErrorCode.FEELBEAT_SERVER_INCORRECT_RESPONSE_FORMAT, e)
        }
    }

    private suspend fun processServerError(content: String) {
        errorReceiver.submitError(
            FeelBeatServerException(Json.decodeFromString<ServerErrorMessage>(content).payload)
        )
    }

    @Synchronized
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
                    settings = initialState.settings,
                    readyMap = initialState.readyMap,
                    stage = RoomStage.LOBBY,
                    audio = null,
                    pointsMap = initialState.players.associateBy({ it.id }, { 0 }),
                    songGuessMap = mapOf(),
                    playerGuessMap = mapOf(),
                    lastGuessStatus = GuessCorrectness.VERIFYING,
                )
            )
        gameStateFlow.value = game?.gameState()
    }

    @Synchronized
    private fun processNewPlayer(content: String) {
        game?.addPlayer(Json.decodeFromString<NewPlayerMessage>(content).payload)
        gameStateFlow.value = game?.gameState()
    }

    private fun processPlayerLeft(content: String) {
        val payload = Json.decodeFromString<PlayerLeftMessage>(content).payload
        synchronized(this) {
            game?.removePlayer(payload.left)
            game?.setAdmin(payload.admin)
            gameStateFlow.value = game?.gameState()
        }
    }

    private fun processPlayerReady(content: String) {
        val payload = Json.decodeFromString<PlayerReadyMessage>(content).payload

        synchronized(this) {
            game?.updateReadyStatus(payload.player, payload.ready)
            gameStateFlow.value = game?.gameState()
        }
    }

    private fun processRoomStage(content: String) {
        val stage = Json.decodeFromString<RoomStageMessage>(content).payload

        synchronized(this) {
            game?.setStage(RoomStage.valueOf(stage))
            gameStateFlow.value = game?.gameState()
        }
    }

    private fun processPlaySong(content: String) {
        val payload = Json.decodeFromString<PlaySongMessage>(content).payload
        val start = Instant.ofEpochSecond(payload.timestamp)

        synchronized(this) {
            game.let {
                if (it == null) {
                    return
                }

                it.scheduleAudio(payload.url, start, Duration.ofMillis(payload.duration))
                it.resetGuessing()
                gameStateFlow.value = it.gameState()
            }
        }
    }

    private fun processPlayerGuess(content: String) {
        val payload = Json.decodeFromString<PlayerGuessMessage>(content).payload

        synchronized(this) {
            game.let {
                if (game == null) {
                    return
                }

                if (payload.songId.isNotEmpty()) {
                    it?.resolveGuess(payload.songId, payload.correct)
                }

                it?.setPlayerGuessResult(payload.playerId, payload.correct)
                it?.addPoints(payload.playerId, payload.points)

                gameStateFlow.value = it?.gameState()
            }
        }
    }

    private fun processCorrectSong(content: String) {
        val correctSongId = Json.decodeFromString<CorrectSongMessage>(content).payload

        synchronized(this) {
            game?.setCorrectSong(correctSongId)
            gameStateFlow.value = game?.gameState()
        }
    }

    private fun processEndGame(content: String) {
        val payload = Json.decodeFromString<EndGameMessage>(content).payload

        synchronized(this) {
            game.let {
                if (it == null) {
                    return
                }

                it.resetGame()
                gameStateFlow.value = it.gameState()
            }
        }

        resultStateFlow.value = payload.results
    }
}
