package com.github.feelbeatapp.androidclient.ui.app.game.startgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartGameViewModel : ViewModel() {
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            val examplePlayers =
                listOf(
                    Player("User123", R.drawable.userimage),
                    Player("User456", R.drawable.userimage),
                    Player("User789", R.drawable.userimage),
                )
            _players.value = examplePlayers
        }
    }
}
