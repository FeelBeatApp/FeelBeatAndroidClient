package com.github.feelbeatapp.androidclient.network.api.responses

import com.github.feelbeatapp.androidclient.model.RoomListView
import kotlinx.serialization.Serializable

@Serializable data class FetchRoomsResponse(val rooms: List<RoomListView>)
