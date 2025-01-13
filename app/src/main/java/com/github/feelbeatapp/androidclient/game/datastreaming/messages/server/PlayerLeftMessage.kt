package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PLAYER_LEFT")
data class PlayerLeftMessage(
    override val type: String = ServerMessageType.PLAYER_LEFT.name,
    val payload: PlayerLeftPayload,
) : ServerMessage()

@Serializable data class PlayerLeftPayload(val left: String, val admin: String)
