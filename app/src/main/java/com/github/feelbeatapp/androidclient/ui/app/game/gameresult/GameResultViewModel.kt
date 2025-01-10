package com.github.feelbeatapp.androidclient.ui.app.game.gameresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.ui.app.uimodel.PlayerWithResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameResultViewModel : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerWithResult>>(emptyList())
    val players: StateFlow<List<PlayerWithResult>> = _players

    init {
        fetchGameResults()
    }

    @SuppressWarnings("MagicNumber")
    private fun fetchGameResults() {
        viewModelScope.launch {}
    }
}
