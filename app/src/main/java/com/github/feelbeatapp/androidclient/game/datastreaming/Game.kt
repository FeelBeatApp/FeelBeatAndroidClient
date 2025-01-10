package com.github.feelbeatapp.androidclient.game.datastreaming

import com.github.feelbeatapp.androidclient.game.model.GameState
import com.github.feelbeatapp.androidclient.game.model.Player

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
}
