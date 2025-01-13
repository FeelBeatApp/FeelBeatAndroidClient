package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SERVER_ERROR")
data class ServerErrorMessage(
    override val type: String = ServerMessageType.SERVER_ERROR.name,
    val payload: String,
) : ServerMessage()
