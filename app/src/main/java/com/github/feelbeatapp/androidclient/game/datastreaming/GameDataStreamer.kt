package com.github.feelbeatapp.androidclient.game.datastreaming

import com.github.feelbeatapp.androidclient.game.model.GameState
import kotlinx.coroutines.flow.StateFlow

interface GameDataStreamer {
    suspend fun joinRoom(roomId: String)

    fun gameStateFlow(): StateFlow<GameState?>
}
