package com.github.feelbeatapp.androidclient.game.datastreaming

import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.Player
import com.github.feelbeatapp.androidclient.game.model.RoomStage

class Game(private var gameState: GameState) {
    fun gameState(): GameState {
        return gameState
    }

    fun addPlayer(player: Player) {
        gameState = gameState.copy(players = gameState.players.plus(player))
    }

    fun removePlayer(playerId: String) {
        gameState = gameState.copy(players = gameState.players.filter { it.id != playerId })
    }

    fun setAdmin(playerId: String) {
        gameState = gameState.copy(adminId = playerId)
    }

    fun setMyReadyStatus(ready: Boolean) {
        gameState = gameState.copy(readyMap = gameState.readyMap.plus(Pair(gameState.me, ready)))
    }

    fun updateReadyStatus(playerId: String, ready: Boolean) {
        gameState = gameState.copy(readyMap = gameState.readyMap.plus(Pair(playerId, ready)))
    }

    fun setStage(stage: RoomStage) {
        gameState = gameState.copy(stage = stage)
    }
}
