package com.github.feelbeatapp.androidclient.network.api.payloads

import com.github.feelbeatapp.androidclient.ui.model.RoomSettings
import io.ktor.http.Url
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomPayload(
    val maxPlayers: Int,
    val turnCount: Int,
    val timePenaltyPerSecond: Int,
    val basePoints: Int,
    val incorrectGuessPenalty: Int,
    val playListId: String,
) {
    companion object {
        fun fromRoomSettings(settings: RoomSettings): CreateRoomPayload {
            return CreateRoomPayload(
                maxPlayers = settings.maxPlayers,
                turnCount = settings.turnCount,
                timePenaltyPerSecond = settings.timePenaltyPerSecond,
                basePoints = settings.basePoints,
                incorrectGuessPenalty = settings.incorrectGuessPenalty,
                playListId = Url(settings.playlistLink).segments.last(),
            )
        }
    }
}
