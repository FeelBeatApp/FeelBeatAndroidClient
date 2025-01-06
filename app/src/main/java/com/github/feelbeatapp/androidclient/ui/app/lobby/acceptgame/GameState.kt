package com.github.feelbeatapp.androidclient.ui.app.lobby.acceptgame

import com.github.feelbeatapp.androidclient.ui.app.model.Player
import com.github.feelbeatapp.androidclient.ui.app.model.Playlist
import com.github.feelbeatapp.androidclient.ui.app.model.Room
import com.github.feelbeatapp.androidclient.ui.app.model.Song


data class GameState(
    val players: List<Player> = emptyList(),
    val songs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlist: Playlist = Playlist(name = "Playlist #1", songs = emptyList()),
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10,
)
