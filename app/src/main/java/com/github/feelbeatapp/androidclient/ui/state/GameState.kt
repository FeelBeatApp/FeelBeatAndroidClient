package com.github.feelbeatapp.androidclient.ui.state

data class GameState(
    val players: List<Player> = emptyList(),
    val songs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlist: Playlist = Playlist(name = "Playlist #1", songs = emptyList()),
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10,
)
