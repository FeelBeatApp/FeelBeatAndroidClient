package com.github.feelbeatapp.androidclient.ui.acceptgame

import com.github.feelbeatapp.androidclient.model.Player
import com.github.feelbeatapp.androidclient.model.Playlist
import com.github.feelbeatapp.androidclient.model.Room
import com.github.feelbeatapp.androidclient.model.Song

data class GameState(
    val players: List<Player> = emptyList(),
    val songs: List<Song> = emptyList(),
    val selectedRoom: Room? = null,
    val playlist: Playlist = Playlist(name = "Playlist #1", songs = emptyList()),
    val snippetDuration: Int = 30,
    val pointsToWin: Int = 10,
)
