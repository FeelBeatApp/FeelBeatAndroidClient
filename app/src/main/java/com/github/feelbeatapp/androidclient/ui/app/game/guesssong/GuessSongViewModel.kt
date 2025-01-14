package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.GuessCorrectness
import com.github.feelbeatapp.androidclient.game.model.Player
import com.github.feelbeatapp.androidclient.game.model.Song
import com.github.feelbeatapp.androidclient.infra.audio.AudioController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch

data class GuessSongState(
    val players: List<PlayerState> = listOf(),
    val query: String = "",
    val songs: List<SongState> = listOf(),
    val songDuration: Long = 0,
    val pointsToWin: Int = 0,
    val cumulatedPoints: Int = 0,
    val pointsMap: Map<String, Int> = mapOf(),
    val lastGuessCorrect: Boolean = false,
)

data class SongState(val song: Song, val status: GuessCorrectness)

data class PlayerState(val player: Player, val status: GuessCorrectness)

@HiltViewModel
class GuessSongViewModel
@Inject
constructor(
    private val gameDataStreamer: GameDataStreamer,
    private val audioController: AudioController,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GuessSongState())
    val uiState = _uiState.asStateFlow()
    val playbackState = audioController.playbackState

    init {
        updateState(gameDataStreamer.gameStateFlow().value)
        viewModelScope.launch { gameDataStreamer.gameStateFlow().collect { updateState(it) } }
    }

    private fun updateState(gameState: GameState?) {
        _uiState.update {
            it.copy(
                players =
                    gameState?.players?.map { player ->
                        PlayerState(
                            player,
                            gameState.playerGuessMap[player.id] ?: GuessCorrectness.UNKNOWN,
                        )
                    } ?: listOf(),
                songs =
                    gameState
                        ?.songs
                        ?.map { song ->
                            SongState(
                                song,
                                gameState.songGuessMap[song.id] ?: GuessCorrectness.UNKNOWN,
                            )
                        }
                        ?.filter { song -> song.status != GuessCorrectness.INCORRECT } ?: listOf(),
                songDuration = gameState?.audio?.duration?.toMillis() ?: 0,
                cumulatedPoints = gameState?.pointsMap?.get(gameState.me) ?: 0,
                pointsMap = gameState?.pointsMap ?: mapOf(),
                lastGuessCorrect = gameState?.lastGuessStatus == GuessCorrectness.CORRECT,
            )
        }
    }

    private fun fuzzySort(query: String): List<SongState> {
        return if (query.isBlank()) _uiState.value.songs
        else
            _uiState.value.songs.sortedByDescending {
                FuzzySearch.ratio(it.song.title.lowercase(), query.lowercase())
            }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(query = query, songs = fuzzySort(query)) }
    }

    fun play() {
        audioController.play()

        _uiState.update {
            it.copy(pointsToWin = gameDataStreamer.gameStateFlow().value?.settings?.basePoints ?: 0)
        }

        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1.seconds)
                val minus =
                    gameDataStreamer.gameStateFlow().value?.settings?.timePenaltyPerSecond ?: 0
                _uiState.update { it.copy(pointsToWin = it.pointsToWin - minus) }

                if (_uiState.value.pointsToWin <= 0) {
                    _uiState.update { it.copy(pointsToWin = 0) }
                    gameDataStreamer.sendGuess("", 0)
                    return@launch
                }
            }
        }
    }

    fun pause() {
        audioController.pause()
    }

    fun seek(to: Long) {
        audioController.seek(to)
    }

    fun guess(songId: String) {
        viewModelScope.launch { gameDataStreamer.sendGuess(songId, uiState.value.pointsToWin) }
    }
}
