package com.github.feelbeatapp.androidclient.ui.guesssong

import androidx.compose.ui.text.input.TextFieldValue
import com.github.feelbeatapp.androidclient.model.PlayerWithResult
import com.github.feelbeatapp.androidclient.model.Playlist
import com.github.feelbeatapp.androidclient.model.Room
import com.github.feelbeatapp.androidclient.model.Song

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
