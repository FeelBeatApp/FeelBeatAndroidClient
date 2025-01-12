package com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels

import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditRoomState(val isAdmin: Boolean = false, val loaded: Boolean = false)

@HiltViewModel
class LobbyRoomSettingsViewModel
@Inject
constructor(private val gameDataStreamer: GameDataStreamer) : RoomSettingsViewModel() {
    private val _editRoomState = MutableStateFlow(EditRoomState())
    val editRoomState = _editRoomState.asStateFlow()

    init {
        updateState(gameDataStreamer.gameStateFlow().value)
        viewModelScope.launch { gameDataStreamer.gameStateFlow().collect { updateState(it) } }
    }

    private fun updateState(gameState: GameState?) {
        _editRoomState.value =
            EditRoomState(isAdmin = gameState?.adminId == gameState?.me, loaded = gameState != null)
    }
}
