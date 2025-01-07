package com.github.feelbeatapp.androidclient.ui.app.uimodel

import com.github.feelbeatapp.androidclient.model.CreateRoomPayload
import io.ktor.http.Url

data class RoomSettings(
    val maxPlayers: Int,
    val turnCount: Int,
    val timePenaltyPerSecond: Int,
    val basePoints: Int,
    val incorrectGuessPenalty: Int,
    val playlistLink: String,
) {
    fun toCreateRoomPayload(): CreateRoomPayload {
        return CreateRoomPayload(
            maxPlayers = maxPlayers,
            turnCount = turnCount,
            timePenaltyPerSecond = timePenaltyPerSecond,
            basePoints = basePoints,
            incorrectGuessPenalty = incorrectGuessPenalty,
            playListId = Url(playlistLink).segments.last(),
        )
    }
}
