package com.github.feelbeatapp.androidclient.api.feelbeat

import com.github.feelbeatapp.androidclient.api.feelbeat.responses.RoomListViewResponse
import com.github.feelbeatapp.androidclient.game.model.RoomSettings

interface FeelBeatApi {
    suspend fun createRoom(payload: RoomSettings): String

    suspend fun fetchRooms(): List<RoomListViewResponse>
}
