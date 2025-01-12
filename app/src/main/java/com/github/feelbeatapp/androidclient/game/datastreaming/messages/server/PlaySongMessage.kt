package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PLAY_SONG")
data class PlaySongMessage(
    override val type: String = ServerMessageType.PLAY_SONG.name,
    val payload: PlaySongPayload,
) : ServerMessage()

@Serializable data class PlaySongPayload(val url: String, val timestamp: Long)
