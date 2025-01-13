package com.github.feelbeatapp.androidclient.ui.app.game.gameresult

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameResultViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    init {
        loadGameResults()
        sortPlayers()
    }

    private fun loadGameResults() {
        viewModelScope.launch {
            gameDataStreamer.gameStateFlow().collect { gameState ->
                _players.value = gameState?.players ?: listOf()
                Log.d("yup", "updating")
            }
        }
    }

    private fun sortPlayers() {
        _players.value = _players.value.sortedByDescending { player -> player.score }
    }
}
