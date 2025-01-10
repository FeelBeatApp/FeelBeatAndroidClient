package com.github.feelbeatapp.androidclient.game.model

data class GameState(
    val roomId: String,
    val playlistName: String,
    val playlistImageUrl: String,
    val adminId: String,
    val players: List<Player>,
    val songs: List<Song>,
)
