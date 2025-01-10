package com.github.feelbeatapp.androidclient.api.feelbeat

import com.github.feelbeatapp.androidclient.api.feelbeat.responses.CreateRoomResponse
import com.github.feelbeatapp.androidclient.api.feelbeat.responses.FetchRoomsResponse
import com.github.feelbeatapp.androidclient.api.feelbeat.responses.RoomListViewResponse
import com.github.feelbeatapp.androidclient.game.model.RoomSettings
import com.github.feelbeatapp.androidclient.infra.auth.AuthManager
import com.github.feelbeatapp.androidclient.infra.error.ErrorCode
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatServerException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KtorFeelBeatApi
@Inject
constructor(
    @Named("API_URL") private val baseUrl: String,
    private val httpClient: HttpClient,
    private val authManager: AuthManager,
) : FeelBeatApi {
    override suspend fun createRoom(payload: RoomSettings): String =
        withContext(Dispatchers.IO) {
            val token = authManager.getAccessToken()

            val res =
                try {
                    httpClient.post("$baseUrl/create") {
                        headers { set("Authorization", "Bearer $token") }
                        contentType(ContentType.Application.Json)
                        setBody(payload)
                    }
                } catch (e: Exception) {
                    when (e) {
                        is IOException,
                        is UnresolvedAddressException ->
                            throw FeelBeatException(ErrorCode.FEELBEAT_SERVER_UNREACHABLE, e)
                        else -> throw e
                    }
                }

            if (res.status != HttpStatusCode.OK) {
                throw FeelBeatServerException(res.bodyAsText().trim())
            }

            val (roomId) =
                try {
                    res.body<CreateRoomResponse>()
                } catch (e: UnsupportedOperationException) {
                    throw FeelBeatException(
                        ErrorCode.FEELBEAT_SERVER_INCORRECT_RESPONSE_FORMAT,
                        "Failed to parse server response",
                        e,
                    )
                }

            roomId
        }

    override suspend fun fetchRooms(): List<RoomListViewResponse> =
        withContext(Dispatchers.IO) {
            val token = authManager.getAccessToken()

            val res =
                try {
                    httpClient.get("$baseUrl/rooms") {
                        headers { set("Authorization", "Bearer $token") }
                    }
                } catch (e: Exception) {
                    when (e) {
                        is IOException,
                        is UnresolvedAddressException ->
                            throw FeelBeatException(ErrorCode.FEELBEAT_SERVER_UNREACHABLE, e)
                        else -> throw e
                    }
                }

            if (res.status != HttpStatusCode.OK) {
                throw FeelBeatServerException(res.bodyAsText().trim())
            }

            val (rooms) =
                try {
                    res.body<FetchRoomsResponse>()
                } catch (e: UnsupportedOperationException) {
                    throw FeelBeatException(
                        ErrorCode.FEELBEAT_SERVER_INCORRECT_RESPONSE_FORMAT,
                        "Failed to parse server response",
                        e,
                    )
                }

            rooms
        }
}
