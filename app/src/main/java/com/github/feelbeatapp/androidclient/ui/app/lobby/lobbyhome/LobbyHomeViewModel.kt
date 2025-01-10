package com.github.feelbeatapp.androidclient.ui.app.lobby.lobbyhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LobbyHomeState(
    val currentRoomId: String? = null,
    val playlistName: String = "Playlist",
    val playlistImageUrl: String = "",
    val adminName: String = "Admin",
    val players: List<Player> = listOf(),
)

@HiltViewModel
class LobbyHomeViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {
    private val _lobbyHomeState = MutableStateFlow(LobbyHomeState())
    val lobbyHomeState = _lobbyHomeState.asStateFlow()

    init {
        updateState(gameDataStreamer.gameStateFlow().value)
        viewModelScope.launch { gameDataStreamer.gameStateFlow().collect { updateState(it) } }
    }

    private fun updateState(gameState: GameState?) {
        if (gameState != null) {
            _lobbyHomeState.value =
                LobbyHomeState(
                    currentRoomId = gameState.roomId,
                    playlistName = gameState.playlistName,
                    playlistImageUrl = gameState.playlistImageUrl,
                    adminName = gameState.players.find { it.id == gameState.adminId }?.name ?: "",
                    players = gameState.players,
                )
        } else {
            _lobbyHomeState.update { it.copy(currentRoomId = null) }
        }
    }

    suspend fun joinRoom(roomId: String) {
        _lobbyHomeState.update { it.copy(currentRoomId = null) }
        gameDataStreamer.joinRoom(roomId)
    }
}
