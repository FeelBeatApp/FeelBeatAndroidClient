package com.github.feelbeatapp.androidclient.ui.app.lobby.acceptgame

import com.github.feelbeatapp.androidclient.ui.app.uimodel.Player
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Playlist
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Room
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Song


data class GameState(
    val players: List<Player> = emptyList(),
    val songs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlist: Playlist = Playlist(name = "Playlist #1", songs = emptyList()),
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10,
)
