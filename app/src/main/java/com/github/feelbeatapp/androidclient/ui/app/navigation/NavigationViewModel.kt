package com.github.feelbeatapp.androidclient.ui.app.navigation

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.model.GuessCorrectness
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {
    val stage = gameDataStreamer.gameStateFlow().map { it?.stage }
    val roomId = gameDataStreamer.gameStateFlow().map { it?.roomId }
    val correctReceived =
        gameDataStreamer.gameStateFlow().map {
            it?.songGuessMap?.any { s -> s.value == GuessCorrectness.CORRECT } ?: false
        }

    val scheduledAudio = gameDataStreamer.gameStateFlow().map { it?.audio }

    val lastGameResult = gameDataStreamer.lastGameResultStateFlow()
}
