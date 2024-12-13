package com.github.feelbeatapp.androidclient.ui.acceptGame

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.home.Room
import com.github.feelbeatapp.androidclient.ui.startGame.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Song(val id: Int, val title: String)
data class Player(val name: String, val image: Int)
data class Playlist(val name: String, val songs: List<Song>)

class AcceptViewModel : ViewModel() {
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val rooms: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _selectedRoom= MutableStateFlow<Room?>(null)
    val selectedRoom: StateFlow<Room?> = _selectedRoom.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _playlist = MutableStateFlow(Playlist(name = "Playlist #1", songs = emptyList()))
    val playlist: StateFlow<Playlist> = _playlist.asStateFlow()

    private val _snippetDuration = MutableStateFlow(30)
    val snippetDuration: StateFlow<Int> = _snippetDuration.asStateFlow()

    private val _pointsToWin = MutableStateFlow(100)
    val pointsToWin: StateFlow<Int> = _pointsToWin.asStateFlow()

    init {
        loadPlayers()
        loadSongs()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            val examplePlayers = listOf(
                Player("User123", R.drawable.userimage),
                Player("User456", R.drawable.userimage),
                Player("User789", R.drawable.userimage)
            )
            _players.value = examplePlayers
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            val exampleSongs = listOf(
                Song(1, "Song 1"),
                Song(2, "Song 2"),
                Song(3, "Song 3"),
                Song(4, "Song 4"),
                Song(5, "Song 5"),
            )
            _songs.value = exampleSongs
        }
    }
}
