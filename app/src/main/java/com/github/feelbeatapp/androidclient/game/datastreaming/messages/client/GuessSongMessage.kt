package com.github.feelbeatapp.androidclient.game.datastreaming.messages.client

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class GuessSongMessage(
    @Required val type: String = ClientMessageType.GUESS_SONG.name,
    val payload: GuessSongPayload,
)

@Serializable data class GuessSongPayload(val id: String, val points: Int)
