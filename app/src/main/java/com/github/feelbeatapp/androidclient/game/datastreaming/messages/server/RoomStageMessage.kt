package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class RoomStageMessage(
    @Required override val type: String = ServerMessageType.ROOM_STAGE.name,
    val payload: String,
) : ServerMessage()
