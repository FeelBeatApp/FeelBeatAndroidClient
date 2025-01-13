package com.github.feelbeatapp.androidclient.game.datastreaming

import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.RoomSettings
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow

interface GameDataStreamer {
    suspend fun joinRoom(roomId: String): Deferred<Unit>

    fun leaveRoom()

    fun gameStateFlow(): StateFlow<GameState?>

    suspend fun updateSettings(settings: RoomSettings)

    suspend fun sendReadyStatus(ready: Boolean)

    suspend fun sendGuess(id: String, points: Int)
}
