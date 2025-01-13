package com.github.feelbeatapp.androidclient.ui.app.game.startgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.Player
import com.github.feelbeatapp.androidclient.infra.audio.AudioController
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StartGameState(
    val loading: Boolean = true,
    val players: List<Player> = listOf(),
    val counter: Int = 5,
)

const val SECOND_MS: Long = 1000

@HiltViewModel
class StartGameViewModel
@Inject
constructor(
    private val gameDataStreamer: GameDataStreamer,
    private val audioController: AudioController,
) : ViewModel() {
    private val _startGameState = MutableStateFlow(StartGameState())
    val startGameState = _startGameState.asStateFlow()

    private var counterLaunched = false

    init {
        updateState(gameDataStreamer.gameStateFlow().value)

        viewModelScope.launch {
            gameDataStreamer.gameStateFlow().collect { gameState ->
                updateState(gameState)
                if (!counterLaunched) {
                    viewModelScope.launch {
                        while (startGameState.value.counter > 0) {
                            delay(SECOND_MS)
                            updateState(gameDataStreamer.gameStateFlow().value)
                        }
                    }
                }
            }
        }
    }

    private fun updateState(gameState: GameState?) {
        _startGameState.value =
            StartGameState(
                players = gameState?.players ?: listOf(),
                loading = gameState?.audio == null,
                counter =
                    if (gameState?.audio != null)
                        Duration.between(Instant.now(), gameState.audio.startAt).seconds.toInt()
                    else 5,
            )

        if (gameState?.audio != null) {
            audioController.loadAudioFromUri(
                gameState.audio.url,
                gameState.audio.duration.toMillis(),
            )
        }
    }
}
