package com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.game.model.RoomSettings
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RoomSettings(
    val maxPlayers: Int,
    val turnCount: Int,
    val timePenaltyPerSecond: Int,
    val basePoints: Int,
    val incorrectGuessPenalty: Int,
    val playlistLink: String,
) {
    fun toRoomSettingsModel(): RoomSettings {
        return RoomSettings(
            maxPlayers = maxPlayers,
            turnCount = turnCount,
            timePenaltyPerSecond = timePenaltyPerSecond,
            basePoints = basePoints,
            incorrectGuessPenalty = incorrectGuessPenalty,
            playlistId = Url(playlistLink).segments.last(),
        )
    }
}

abstract class RoomSettingsViewModel : ViewModel() {
    protected val mRoomSettings =
        MutableStateFlow(
            RoomSettings(
                maxPlayers = 4,
                turnCount = 5,
                timePenaltyPerSecond = 5,
                basePoints = 500,
                incorrectGuessPenalty = 100,
                playlistLink = "",
            )
        )
    val roomSettings = mRoomSettings.asStateFlow()

    fun setMaxPlayers(value: Int) {
        mRoomSettings.update { it.copy(maxPlayers = value) }
    }

    fun setTurnCount(value: Int) {
        mRoomSettings.update { it.copy(turnCount = value) }
    }

    fun setTimePenaltyPerSecond(value: Int) {
        mRoomSettings.update { it.copy(timePenaltyPerSecond = value) }
    }

    fun setBasePoints(value: Int) {
        mRoomSettings.update { it.copy(basePoints = value) }
    }

    fun setIncorrectGuessPenalty(value: Int) {
        mRoomSettings.update { it.copy(incorrectGuessPenalty = value) }
    }

    fun setPlaylistLink(value: String) {
        mRoomSettings.update { it.copy(playlistLink = value) }
    }
}
