package com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels

import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.infra.error.ErrorCode
import com.github.feelbeatapp.androidclient.infra.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val BASE_PLAYLIST_LINK = "https://open.spotify.com/playlist"

data class EditRoomState(val isAdmin: Boolean = false, val loaded: Boolean = false)

@HiltViewModel
class LobbyRoomSettingsViewModel
@Inject
constructor(
    private val gameDataStreamer: GameDataStreamer,
    private val errorReceiver: ErrorReceiver,
) : RoomSettingsViewModel() {
    private val _editRoomState = MutableStateFlow(EditRoomState())
    val editRoomState = _editRoomState.asStateFlow()

    private var appliedSettings: RoomSettings
    val isApplied = MutableStateFlow(true)

    init {
        updateState(gameDataStreamer.gameStateFlow().value)
        appliedSettings = mRoomSettings.value

        viewModelScope.launch { gameDataStreamer.gameStateFlow().collect { updateState(it) } }

        viewModelScope.launch { roomSettings.collect { isApplied.value = appliedSettings == it } }
    }

    private fun updateState(gameState: GameState?) {
        _editRoomState.value =
            EditRoomState(isAdmin = gameState?.adminId == gameState?.me, loaded = gameState != null)

        if (gameState != null) {
            appliedSettings =
                RoomSettings(
                    maxPlayers = gameState.settings.maxPlayers,
                    turnCount = gameState.settings.turnCount,
                    basePoints = gameState.settings.basePoints,
                    incorrectGuessPenalty = gameState.settings.incorrectGuessPenalty,
                    timePenaltyPerSecond = gameState.settings.timePenaltyPerSecond,
                    playlistLink = "${BASE_PLAYLIST_LINK}/${gameState.settings.playlistId}",
                )

            mRoomSettings.value = appliedSettings
            isApplied.value = true
        }
    }

    fun apply() {
        val currentPlayersCount = gameDataStreamer.gameStateFlow().value?.players?.size ?: 0
        if (currentPlayersCount > roomSettings.value.maxPlayers) {
            setMaxPlayers(currentPlayersCount)
            viewModelScope.launch {
                errorReceiver.submitError(FeelBeatException(ErrorCode.MAX_PLAYERS_TOO_SMALL))
            }
            return
        }

        _editRoomState.update { it.copy(loaded = false) }
        viewModelScope.launch {
            gameDataStreamer.updateSettings(roomSettings.value.toRoomSettingsModel())
        }
    }
}
