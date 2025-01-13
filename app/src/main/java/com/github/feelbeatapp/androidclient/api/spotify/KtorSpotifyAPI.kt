package com.github.feelbeatapp.androidclient.api.spotify

import com.github.feelbeatapp.androidclient.api.spotify.responses.ProfileResponse
import com.github.feelbeatapp.androidclient.infra.auth.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val SPOTIFY_API_URL = "https://api.spotify.com/v1"

class KtorSpotifyAPI
@Inject
constructor(private val httpClient: HttpClient, private val authManager: AuthManager) : SpotifyAPI {
    override suspend fun getProfile(): ProfileResponse =
        withContext(Dispatchers.IO) {
            val accessToken = authManager.getAccessToken()

            val response =
                httpClient.get("$SPOTIFY_API_URL/me") {
                    headers { append("Authorization", "Bearer $accessToken") }
                }

            response.body<ProfileResponse>()
        }
}
