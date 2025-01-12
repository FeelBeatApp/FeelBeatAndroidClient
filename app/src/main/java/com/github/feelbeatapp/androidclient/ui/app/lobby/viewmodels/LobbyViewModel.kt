package com.github.feelbeatapp.androidclient.ui.app.lobby.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LobbyState(val loading: Boolean = false, val joinFailed: Boolean = false)

@HiltViewModel
class LobbyViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {
    private val _lobbyState = MutableStateFlow(LobbyState())
    val lobbyState = _lobbyState.asStateFlow()

    fun joinRoom(roomId: String) {
        if (lobbyState.value.loading) {
            return
        }

        _lobbyState.update { it.copy(loading = true, joinFailed = false) }
        viewModelScope.launch {
            try {
                gameDataStreamer.joinRoom(roomId).await()
                _lobbyState.update { it.copy(loading = false, joinFailed = false) }
            } catch (_: FeelBeatException) {
                _lobbyState.update { it.copy(loading = false, joinFailed = true) }
            }
        }
    }

    fun reset() {
        _lobbyState.value = LobbyState(loading = false, joinFailed = false)
    }
}
