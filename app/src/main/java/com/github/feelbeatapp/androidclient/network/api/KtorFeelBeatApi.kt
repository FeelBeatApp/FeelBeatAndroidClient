package com.github.feelbeatapp.androidclient.network.api

import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.error.ErrorCode
import com.github.feelbeatapp.androidclient.error.FeelBeatException
import com.github.feelbeatapp.androidclient.network.api.payloads.CreateRoomPayload
import com.github.feelbeatapp.androidclient.network.api.responses.CreateRoomResponse
import com.github.feelbeatapp.androidclient.ui.model.RoomSettings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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

class KtorFeelBeatApi
@Inject
constructor(
    @Named("API_URL") private val baseUrl: String,
    private val httpClient: HttpClient,
    private val authManager: AuthManager,
) : FeelBeatApi {
    override suspend fun createRoom(settings: RoomSettings): String {
        val payload = CreateRoomPayload.fromRoomSettings(settings)
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
            throw FeelBeatException(ErrorCode.FEELBEAT_SERVER_ERROR, res.bodyAsText())
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

        return roomId
    }
}
