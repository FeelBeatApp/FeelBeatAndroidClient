package com.github.feelbeatapp.androidclient.ui.app.navigation

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class NavigationViewModel @Inject constructor(private val gameDataStreamer: GameDataStreamer) :
    ViewModel() {
    val stage = gameDataStreamer.gameStateFlow().map { it?.stage }
    val roomId = gameDataStreamer.gameStateFlow().map { it?.roomId }
}
