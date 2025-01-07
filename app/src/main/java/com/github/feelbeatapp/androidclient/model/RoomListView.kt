package com.github.feelbeatapp.androidclient.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomListView(
    val id: String,
    val name: String,
    val players: Int,
    val maxPlayers: Int,
    val imageUrl: String,
)
