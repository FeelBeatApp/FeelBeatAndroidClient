package com.github.feelbeatapp.androidclient.network.spotify

import com.github.feelbeatapp.androidclient.network.spotify.responses.ProfileResponse

interface SpotifyAPI {
    suspend fun getProfile(): ProfileResponse
}
