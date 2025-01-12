package com.github.feelbeatapp.androidclient.game.datastreaming.messages.client

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ReadyStatusMessage(
    @Required val type: String = ClientMessageType.READY_STATUS.name,
    val payload: Boolean,
)
