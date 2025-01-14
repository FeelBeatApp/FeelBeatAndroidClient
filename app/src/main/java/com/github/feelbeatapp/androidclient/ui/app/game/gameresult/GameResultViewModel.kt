package com.github.feelbeatapp.androidclient.ui.app.game.gameresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.PlayerFinalScore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameResultState(val roomId: String = "", val results: List<PlayerFinalScore> = listOf())

@HiltViewModel
class GameResultViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {
    private val _gameResultState = MutableStateFlow(GameResultState())
    val gameResultState = _gameResultState.asStateFlow()

    init {
        updateRoomId(gameDataStreamer.gameStateFlow().value)
        viewModelScope.launch { gameDataStreamer.gameStateFlow().collect { updateRoomId(it) } }

        _gameResultState.update {
            it.copy(results = gameDataStreamer.lastGameResultStateFlow().value)
        }
        viewModelScope.launch {
            gameDataStreamer.lastGameResultStateFlow().collect { results ->
                _gameResultState.update { it.copy(results = results) }
            }
        }
    }

    private fun updateRoomId(gameState: GameState?) {
        if (gameState != null) {
            _gameResultState.update { it.copy(roomId = gameState.roomId) }
        }
    }
}
