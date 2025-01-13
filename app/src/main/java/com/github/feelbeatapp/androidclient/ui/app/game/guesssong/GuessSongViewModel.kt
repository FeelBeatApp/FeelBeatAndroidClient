package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GameState
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
    val players: List<Player> = listOf(),
    val query: String = "",
    val songs: List<Song> = listOf(),
    val songDuration: Long = 0,
    val pointsToWin: Int = 0,
    val cumulatedPoints: Int = 0,
)

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
                players = gameState?.players ?: listOf(),
                songs = gameState?.songs ?: listOf(),
                songDuration = gameState?.audio?.duration?.toMillis() ?: 0,
                cumulatedPoints = gameState?.pointsMap?.get(gameState.me) ?: 0,
            )
        }
    }

    private fun fuzzySort(query: String): List<Song> {
        return if (query.isBlank()) _uiState.value.songs
        else
            _uiState.value.songs.sortedByDescending {
                FuzzySearch.ratio(it.title.lowercase(), query.lowercase())
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
            }
        }
    }

    fun pause() {
        audioController.pause()
    }

    fun seek(to: Long) {
        audioController.seek(to)
    }
}
