package com.github.feelbeatapp.androidclient.network.api.payloads

import com.github.feelbeatapp.androidclient.model.RoomSettings
import kotlinx.serialization.Serializable

@Serializable data class CreateRoomPayload(val roomSettings: RoomSettings)
