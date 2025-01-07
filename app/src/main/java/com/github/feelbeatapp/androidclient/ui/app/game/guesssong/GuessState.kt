package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import androidx.compose.ui.text.input.TextFieldValue
import com.github.feelbeatapp.androidclient.ui.app.uimodel.PlayerWithResult
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Playlist
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Room
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Song

data class GuessState(
    val players: List<PlayerWithResult> = emptyList(),
    val songs: List<Song> = emptyList(),
    val filteredSongs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlist: Playlist = Playlist(name = "Playlist #1", songs = emptyList()),
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10,
    val searchQuery: TextFieldValue = TextFieldValue(""),
    val currentSong: Song? = null,
)
