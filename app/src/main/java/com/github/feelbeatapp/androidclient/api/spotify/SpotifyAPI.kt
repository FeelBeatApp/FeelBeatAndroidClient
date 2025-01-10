package com.github.feelbeatapp.androidclient.api.spotify

import com.github.feelbeatapp.androidclient.api.spotify.responses.ProfileResponse

interface SpotifyAPI {
    suspend fun getProfile(): ProfileResponse
}
