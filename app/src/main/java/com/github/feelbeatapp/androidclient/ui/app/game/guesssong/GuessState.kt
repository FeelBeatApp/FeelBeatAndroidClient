package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import androidx.compose.ui.text.input.TextFieldValue
import com.github.feelbeatapp.androidclient.game.model.Song
import com.github.feelbeatapp.androidclient.ui.app.uimodel.PlayerWithResult
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Room

data class GuessState(
    val players: List<PlayerWithResult> = emptyList(),
    val songs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlistName: String = "",
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10,
    val searchQuery: TextFieldValue = TextFieldValue(""),
    val currentSong: Song? = null,
)
