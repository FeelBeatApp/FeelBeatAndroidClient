package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PLAYER_READY")
data class PlayerReadyMessage(
    override val type: String = ServerMessageType.PLAYER_READY.name,
    val payload: PlayerReadyPayload,
) : ServerMessage()

@Serializable data class PlayerReadyPayload(val player: String, val ready: Boolean)
