package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import kotlinx.serialization.Serializable

enum class ServerMessageType {
    INITIAL,
    NEW_PLAYER,
    PLAYER_LEFT,
    SERVER_ERROR,
    PLAYER_READY,
    ROOM_STAGE,
    PLAY_SONG,
    PLAYER_GUESS,
    CORRECT_SONG,
    END_GAME,
}

@Serializable
sealed class ServerMessage {
    abstract val type: String
}
