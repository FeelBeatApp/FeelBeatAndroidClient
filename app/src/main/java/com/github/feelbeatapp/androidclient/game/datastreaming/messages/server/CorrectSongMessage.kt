package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.Serializable

@Serializable
data class CorrectSongMessage(
    override val type: String = ServerMessageType.CORRECT_SONG.name,
    val payload: String,
) : ServerMessage()
