package com.github.feelbeatapp.androidclient.ui.roomSettings

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.ui.home.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RoomSettingsViewModel : ViewModel() {

    private val _maxPlayers = MutableStateFlow(0)
    val maxPlayers: StateFlow<Int> = _maxPlayers.asStateFlow()

    private val _snippetDuration = MutableStateFlow(0)
    val snippetDuration: StateFlow<Int> = _snippetDuration.asStateFlow()

    private val _pointsToWin = MutableStateFlow(0)
    val pointsToWin: StateFlow<Int> = _pointsToWin.asStateFlow()

    private val _playlistLink = MutableStateFlow("")
    val playlistLink: StateFlow<String>
        get() = _playlistLink.asStateFlow()

    fun loadRoomSettings(room: Room) {
        _maxPlayers.value = room.maxPlayers
        _snippetDuration.value = room.snippetDuration
        _pointsToWin.value = room.pointsToWin
        _playlistLink.value = room.playlistLink
    }

    fun setMaxPlayers(value: Int) {
        _maxPlayers.value = value
    }

    fun setSnippetDuration(value: Int) {
        _snippetDuration.value = value
    }

    fun setPointsToWin(value: Int) {
        _pointsToWin.value = value
    }

    fun setPlaylistLink(value: String) {
        _playlistLink.value = value
    }
}
