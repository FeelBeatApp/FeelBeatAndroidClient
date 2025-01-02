package com.github.feelbeatapp.androidclient.model

data class Room(
    val id: Int,
    val name: String,
    val maxPlayers: Int,
    val snippetDuration: Int,
    val pointsToWin: Int,
    val playlistLink: String,
)
