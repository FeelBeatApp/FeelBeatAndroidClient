package com.github.feelbeatapp.androidclient.api.feelbeat.responses

import kotlinx.serialization.Serializable

@Serializable data class FetchRoomsResponse(val rooms: List<RoomListViewResponse>)
