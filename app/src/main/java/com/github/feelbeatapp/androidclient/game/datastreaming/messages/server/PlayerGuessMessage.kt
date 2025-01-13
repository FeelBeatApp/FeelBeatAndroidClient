package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PLAYER_GUESS")
data class PlayerGuessMessage(
    override val type: String = ServerMessageType.PLAYER_GUESS.name,
    val payload: PlayerGuessPayload,
) : ServerMessage()

@Serializable
data class PlayerGuessPayload(
    val correct: Boolean,
    val points: Int,
    val playerId: String,
    val songId: String,
)
