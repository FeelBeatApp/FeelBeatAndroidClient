package com.github.feelbeatapp.androidclient.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.network.fullduplex.NetworkAgent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameState(val textInput: String)

@HiltViewModel
class GameViewModel @Inject constructor(private val socket: NetworkAgent) : ViewModel() {
    private val _state = MutableStateFlow(GameState(""))
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            socket.receiveFlow().collect { msg -> _state.update { GameState(msg) } }
        }
    }

    fun setText(string: String) {
        _state.update { GameState(string) }

        viewModelScope.launch { socket.sendMessage(string) }
    }
}
