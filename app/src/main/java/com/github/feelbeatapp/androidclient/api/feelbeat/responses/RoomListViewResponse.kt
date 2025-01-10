package com.github.feelbeatapp.androidclient.api.feelbeat.responses

import kotlinx.serialization.Serializable

@Serializable
data class RoomListViewResponse(
    val id: String,
    val name: String,
    val players: Int,
    val maxPlayers: Int,
    val imageUrl: String,
)
