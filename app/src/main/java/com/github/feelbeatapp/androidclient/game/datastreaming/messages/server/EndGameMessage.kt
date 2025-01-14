package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import com.github.feelbeatapp.androidclient.game.model.PlayerFinalScore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("END_GAME")
data class EndGameMessage(
    override val type: String = ServerMessageType.END_GAME.name,
    val payload: EndGamePayload,
) : ServerMessage()

@Serializable data class EndGamePayload(val results: List<PlayerFinalScore>)
