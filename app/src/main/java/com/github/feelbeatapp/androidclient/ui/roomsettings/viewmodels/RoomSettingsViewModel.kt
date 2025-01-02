package com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.model.RoomSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class RoomSettingsViewModel : ViewModel() {
    private val _roomSettings =
        MutableStateFlow(
            RoomSettings(maxPlayers = 0, snippetDuration = 0, pointsToWin = 0, playlistLink = "")
        )
    val roomSettings = _roomSettings.asStateFlow()

    fun setMaxPlayers(value: Int) {
        _roomSettings.update { it.copy(maxPlayers = value) }
    }

    fun setSnippetDuration(value: Int) {
        _roomSettings.update { it.copy(snippetDuration = value) }
    }

    fun setPointsToWin(value: Int) {
        _roomSettings.update { it.copy(pointsToWin = value) }
    }

    fun setPlaylistLink(value: String) {
        _roomSettings.update { it.copy(playlistLink = value) }
    }
}
