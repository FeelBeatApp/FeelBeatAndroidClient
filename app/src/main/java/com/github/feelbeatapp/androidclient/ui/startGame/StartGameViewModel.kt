package com.github.feelbeatapp.androidclient.ui.startGame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Player(val name: String, val image: Int)

class StartGameViewModel : ViewModel() {
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            val examplePlayers = listOf(
                Player("User123", R.drawable.userimage),
                Player("User456", R.drawable.userimage),
                Player("User789", R.drawable.userimage)
            )
            _players.value = examplePlayers
        }
    }
}
