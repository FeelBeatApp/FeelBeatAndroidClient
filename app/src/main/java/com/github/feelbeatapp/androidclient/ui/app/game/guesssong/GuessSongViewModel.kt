package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.model.Player
import com.github.feelbeatapp.androidclient.ui.app.model.PlayerWithResult
import com.github.feelbeatapp.androidclient.ui.app.model.Song
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch

class GuessSongViewModel : ViewModel() {
    private val _guessState = MutableStateFlow(GuessState())
    val guessState: StateFlow<GuessState> = _guessState.asStateFlow()

    private val _timeLeft = MutableStateFlow(_guessState.value.snippetDuration)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded: StateFlow<Boolean> = _gameEnded.asStateFlow()

    init {
        loadPlayers()
        loadPlaylist()
        startTimer()
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
                    Song(1, "Hello"),
                    Song(2, "Highway to Hell"),
                    Song(3, "Hell's Comin' with Me"),
                    Song(4, "Riptide"),
                    Song(5, "Rozmowa"),
                    Song(6, "Rower"),
                    Song(7, "R"),
                    Song(8, "Róż"),
                    Song(9, "Rota"),
                    Song(10, "Szarość i Róż"),
                )
            _guessState.value =
                _guessState.value.copy(
                    songs = examplePlaylist,
                    filteredSongs = examplePlaylist,
                    currentSong = examplePlaylist[0],
                )
        }
    }

    @SuppressWarnings("MagicNumber")
    private fun startTimer() {
        viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.emit(_timeLeft.value - 1)
            }
            _gameEnded.value = true
        }
    }

    fun updateSearchQuery(query: TextFieldValue) {
        _guessState.value = _guessState.value.copy(searchQuery = query)
        performSearch(query.text)
    }

    @SuppressWarnings("MagicNumber")
    private fun performSearch(query: String) {
        val sortedList =
            if (query.isBlank()) {
                _guessState.value.songs
            } else {
                _guessState.value.songs.sortedByDescending { song ->
                    FuzzySearch.ratio(song.title.lowercase(), query.lowercase())
                }
            }
        _guessState.value = _guessState.value.copy(filteredSongs = sortedList)
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
