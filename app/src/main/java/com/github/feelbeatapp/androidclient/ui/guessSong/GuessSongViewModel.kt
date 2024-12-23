package com.github.feelbeatapp.androidclient.ui.guessSong

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.acceptGame.Playlist
import com.github.feelbeatapp.androidclient.ui.acceptGame.Song
import com.github.feelbeatapp.androidclient.ui.home.Room
import com.github.feelbeatapp.androidclient.ui.startGame.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerWithResult(val player: Player, val resultStatus: ResultStatus, val points: Int)

data class GuessState(
    val players: List<PlayerWithResult> = emptyList(),
    val songs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlist: Playlist = Playlist(name = "Playlist #1", songs = emptyList()),
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10,
    val searchQuery: TextFieldValue = TextFieldValue(""),
    val currentSong: Song? = null,
)

class GuessSongViewModel : ViewModel() {
    private val _guessState = MutableStateFlow(GuessState())
    val guessState: StateFlow<GuessState> = _guessState.asStateFlow()

    init {
        loadPlayers()
        loadPlaylist()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            val examplePlayers =
                listOf(
                    PlayerWithResult(
                        Player("User123", R.drawable.userimage),
                        ResultStatus.CORRECT,
                        0,
                    ),
                    PlayerWithResult(
                        Player("User456", R.drawable.userimage),
                        ResultStatus.WRONG,
                        0,
                    ),
                    PlayerWithResult(
                        Player("User789", R.drawable.userimage),
                        ResultStatus.NORESPONSE,
                        0,
                    ),
                )
            _guessState.value = _guessState.value.copy(players = examplePlayers)
        }
    }

    @SuppressWarnings("MagicNumber")
    private fun loadPlaylist() {
        viewModelScope.launch {
            val examplePlaylist =
                listOf(
                    Song(1, "Song 1"),
                    Song(2, "Song 2"),
                    Song(3, "Song 3"),
                    Song(4, "Song 4"),
                    Song(5, "Song 5"),
                    Song(6, "Song 6"),
                )
            _guessState.value =
                _guessState.value.copy(songs = examplePlaylist, currentSong = examplePlaylist[0])
        }
    }

    fun updateSearchQuery(newQuery: TextFieldValue) {
        _guessState.value = _guessState.value.copy(searchQuery = newQuery)
    }

    fun submitAnswer(playerName: String, isCorrect: Boolean) {
        viewModelScope.launch {
            val updatedPlayers =
                _guessState.value.players.map { playerWithResult ->
                    if (playerWithResult.player.name == playerName) {
                        val newPoints = playerWithResult.points + if (isCorrect) 1 else 0
                        playerWithResult.copy(
                            resultStatus =
                                if (isCorrect) ResultStatus.CORRECT else ResultStatus.WRONG,
                            points = newPoints,
                        )
                    } else {
                        playerWithResult
                    }
                }
            _guessState.value = _guessState.value.copy(players = updatedPlayers)
        }
    }

    fun setCurrentSong(song: Song) {
        _guessState.value = _guessState.value.copy(currentSong = song)
    }
}
