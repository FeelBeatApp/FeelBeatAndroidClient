package com.github.feelbeatapp.androidclient.ui.guessSong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.startGame.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Zmienic status na enuma?
data class Player1(val name: String, val image: Int, val status: String)
data class Song(val id: Int, val name: String, val artist: String)
data class Result(val isCorrect: Boolean, val points: Int)

class GuessSongViewModel : ViewModel() {
    private val _players = MutableStateFlow<List<Player1>>(emptyList())
    val players: StateFlow<List<Player1>> = _players

    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist: StateFlow<List<Song>> = _playlist

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    private val _result = MutableStateFlow<Result?>(null)
    val result: StateFlow<Result?> = _result

    init {
        loadPlayers()
        loadPlaylist()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            val examplePlayers = listOf(
                Player1("User123", R.drawable.userimage, "WRONG"),
                Player1("User456", R.drawable.userimage, "CORRECT"),
                Player1("User789", R.drawable.userimage, "WRONG")
            )
            _players.value = examplePlayers
        }
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            val examplePlaylist = listOf(
                Song(1, "Song 1", "artist1"),
                Song(2, "Song 2", "artist1"),
                Song(3, "Song 3", "artist1"),
                Song(4, "Song 4", "artist1"),
                Song(5, "Song 5", "artist1"),
                Song(6, "Song 6", "artist1")
            )
            _playlist.value = examplePlaylist
        }
    }

    fun submitAnswer(isCorrect: Boolean) {
        viewModelScope.launch {
            val points = if (isCorrect) 10 else 0
            _result.value = Result(isCorrect = isCorrect, points = points)
        }
    }
}
