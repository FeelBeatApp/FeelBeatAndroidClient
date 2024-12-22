package com.github.feelbeatapp.androidclient.ui.acceptGame

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

data class Playlist(val name: String, val songs: List<Song>)

data class GameState(
    val players: List<Player> = emptyList(),
    val songs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlist: Playlist = Playlist(name = "Playlist #1", songs = emptyList()),
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10
)

class AcceptGameViewModel : ViewModel() {
  private val _gameState = MutableStateFlow(GameState())
  val gameState: StateFlow<GameState> = _gameState.asStateFlow()

  init {
    loadPlayers()
    loadSongs()
  }

  private fun loadPlayers() {
    viewModelScope.launch {
      val examplePlayers =
          listOf(
              Player("User123", R.drawable.userimage),
              Player("User456", R.drawable.userimage),
              Player("User789", R.drawable.userimage))
      _gameState.emit(_gameState.value.copy(players = examplePlayers))
    }
  }

  private fun loadSongs() {
    viewModelScope.launch {
      val exampleSongs =
          listOf(
              Song(1, "Song 1"),
              Song(2, "Song 2"),
              Song(3, "Song 3"),
              Song(4, "Song 4"),
              Song(5, "Song 5"))
      _gameState.emit(_gameState.value.copy(songs = exampleSongs))
    }
  }
}
