package com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.model.RoomSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class RoomSettingsViewModel : ViewModel() {
    private val _roomSettings =
        MutableStateFlow(
            RoomSettings(
                maxPlayers = 0,
                turnCount = 5,
                timePenaltyPerSecond = 5,
                basePoints = 100,
                incorrectGuessPenalty = 20,
                playlistLink = "",
            )
        )
    val roomSettings = _roomSettings.asStateFlow()

    fun setMaxPlayers(value: Int) {
        _roomSettings.update { it.copy(maxPlayers = value) }
    }

    fun setTurnCount(value: Int) {
        _roomSettings.update { it.copy(turnCount = value) }
    }

    fun setTimePenaltyPerSecond(value: Int) {
        _roomSettings.update { it.copy(timePenaltyPerSecond = value) }
    }

    fun setBasePoints(value: Int) {
        _roomSettings.update { it.copy(basePoints = value) }
    }

    fun setIncorrectGuessPenalty(value: Int) {
        _roomSettings.update { it.copy(incorrectGuessPenalty = value) }
    }

    fun setPlaylistLink(value: String) {
        _roomSettings.update { it.copy(playlistLink = value) }
    }
}
