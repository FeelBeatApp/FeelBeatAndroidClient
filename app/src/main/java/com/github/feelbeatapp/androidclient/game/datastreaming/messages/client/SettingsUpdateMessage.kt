package com.github.feelbeatapp.androidclient.game.datastreaming.messages.client

import com.github.feelbeatapp.androidclient.game.model.RoomSettings
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class SettingsUpdateMessage(
    @Required val type: String = ClientMessageType.SETTINGS_UPDATE.name,
    val payload: SettingsUpdatePayload,
)

@Serializable data class SettingsUpdatePayload(val settings: RoomSettings, val token: String)
