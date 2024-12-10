package com.github.feelbeatapp.androidclient.network.spotify

import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.network.spotify.responses.ProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import javax.inject.Inject

const val SPOTIFY_API_URL = "https://api.spotify.com/v1"

class KtorSpotifyAPI
@Inject
constructor(private val httpClient: HttpClient, private val authManager: AuthManager) : SpotifyAPI {
    override suspend fun getProfile(): ProfileResponse {
        val accessToken = authManager.getAccessToken()

        val response =
            httpClient.get("$SPOTIFY_API_URL/me") {
                headers { append("Authorization", "Bearer $accessToken") }
            }

        return response.body<ProfileResponse>()
    }
}
