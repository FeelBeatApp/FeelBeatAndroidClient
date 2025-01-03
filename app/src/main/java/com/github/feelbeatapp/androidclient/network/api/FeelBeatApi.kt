package com.github.feelbeatapp.androidclient.network.api

import com.github.feelbeatapp.androidclient.model.RoomSettings

interface FeelBeatApi {
    suspend fun createRoom(settings: RoomSettings): String
}
