package com.github.feelbeatapp.androidclient.game.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomSettings(
    val maxPlayers: Int,
    val turnCount: Int,
    val timePenaltyPerSecond: Int,
    val basePoints: Int,
    val incorrectGuessPenalty: Int,
    val playlistId: String,
)
