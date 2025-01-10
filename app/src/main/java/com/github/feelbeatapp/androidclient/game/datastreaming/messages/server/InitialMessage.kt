package com.github.feelbeatapp.androidclient.game.datastreaming.messages.server

import com.github.feelbeatapp.androidclient.game.model.Player
import com.github.feelbeatapp.androidclient.game.model.RoomSettings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

@Serializable
@SerialName("INITIAL")
data class InitialMessage(
    override val type: String = ServerMessageType.INITIAL.name,
    val payload: InitialGameState,
) : ServerMessage()

@Serializable
data class InitialGameState(
    val id: String,
    val me: String,
    val admin: String,
    val playlist: Playlist,
    val players: List<Player>,
    val settings: RoomSettings,
)

@Serializable data class Playlist(val name: String, val imageUrl: String, val songs: List<Song>)

@Serializable
data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val durationSec: Int,
) {
    fun toSongModel(): com.github.feelbeatapp.androidclient.game.model.Song {
        return com.github.feelbeatapp.androidclient.game.model.Song(
            id = id,
            title = title,
            artist = artist,
            imageUrl = imageUrl,
            duration = durationSec.seconds,
        )
    }
}

