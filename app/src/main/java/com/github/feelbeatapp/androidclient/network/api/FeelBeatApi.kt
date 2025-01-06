package com.github.feelbeatapp.androidclient.network.api

import com.github.feelbeatapp.androidclient.ui.app.model.RoomSettings


interface FeelBeatApi {
    suspend fun createRoom(settings: RoomSettings): String
}
