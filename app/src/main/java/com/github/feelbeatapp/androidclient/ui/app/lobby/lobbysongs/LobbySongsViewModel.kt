package com.github.feelbeatapp.androidclient.ui.app.lobby.lobbysongs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LobbySongsViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {
    private val _songs =
        MutableStateFlow<List<Song>>(
            listOf(
                Song(
                    "asdf",
                    "Aerials",
                    "System of a down",
                    "https://upload.wikimedia.org/wikipedia/it/2/20/Aerials.png",
                    320.seconds,
                )
            )
        )
    val songs = _songs.asStateFlow()

    init {
        viewModelScope.launch {
            gameDataStreamer.gameStateFlow().collect { gameState ->
                _songs.value = gameState?.songs ?: listOf()
                Log.d("yup", "updating")
            }
        }
    }
}
