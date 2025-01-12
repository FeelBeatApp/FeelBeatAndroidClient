package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.Song
import com.github.feelbeatapp.androidclient.ui.app.uimodel.PlayerWithResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch

@HiltViewModel
class GuessSongViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {
    private val _guessState = MutableStateFlow(GuessState())
    val guessState: StateFlow<GuessState> = _guessState.asStateFlow()

    private val _timeLeft = MutableStateFlow(_guessState.value.snippetDuration)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded: StateFlow<Boolean> = _gameEnded.asStateFlow()

    init {
        loadGuessState()
        startTimer()
    }

    private fun loadGuessState() {
        viewModelScope.launch {
            gameDataStreamer.gameStateFlow().collect { gameState ->
                val updatedPlayers =
                    gameState?.players?.map { player ->
                        PlayerWithResult(
                            player = player,
                            resultStatus = ResultStatus.NORESPONSE,
                            points = 0,
                        )
                    } ?: listOf()

                _guessState.value =
                    _guessState.value.copy(
                        playlistName = gameState?.playlistName.toString(),
                        songs = gameState?.songs ?: listOf(),
                        players = updatedPlayers,
                    )
                Log.d("yup", "updating")
            }
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
        _guessState.value = _guessState.value.copy(songs = sortedList)
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
