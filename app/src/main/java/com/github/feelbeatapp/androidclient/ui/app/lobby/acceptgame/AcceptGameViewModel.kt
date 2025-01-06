package com.github.feelbeatapp.androidclient.ui.app.lobby.acceptgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.model.Player
import com.github.feelbeatapp.androidclient.ui.app.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                    Player("User789", R.drawable.userimage),
                )

            _gameState.update { it.copy(players = examplePlayers) }
        }
    }

    @SuppressWarnings("MagicNumber")
    private fun loadSongs() {
        viewModelScope.launch {
            val exampleSongs =
                listOf(
                    Song(1, "Song 1"),
                    Song(2, "Song 2"),
                    Song(3, "Song 3"),
                    Song(4, "Song 4"),
                    Song(5, "Song 5"),
                )
            _gameState.update { it.copy(songs = exampleSongs) }
        }
    }
}
