package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import com.github.feelbeatapp.androidclient.game.model.Player
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NEW_PLAYER")
data class NewPlayerMessage(
    override val type: String = ServerMessageType.NEW_PLAYER.name,
    val payload: Player,
) : ServerMessage()
