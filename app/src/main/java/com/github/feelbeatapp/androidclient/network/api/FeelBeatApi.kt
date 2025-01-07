package com.github.feelbeatapp.androidclient.network.api

import com.github.feelbeatapp.androidclient.model.CreateRoomPayload
import com.github.feelbeatapp.androidclient.model.RoomListView

interface FeelBeatApi {
    suspend fun createRoom(payload: CreateRoomPayload): String

    suspend fun fetchRooms(): List<RoomListView>
}
