package com.github.feelbeatapp.androidclient.ui.newRoomSettings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewRoomSettingsViewModel : ViewModel() {

    private val _maxPlayers = MutableStateFlow(0)
    val maxPlayers: StateFlow<Int> = _maxPlayers

    private val _snippetDuration = MutableStateFlow(0)
    val snippetDuration: StateFlow<Int> = _snippetDuration

    private val _pointsToWin = MutableStateFlow(0)
    val pointsToWin: StateFlow<Int> = _pointsToWin

    private val _playlistLink = MutableStateFlow("")
    val playlistLink: StateFlow<String> get() = _playlistLink


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
