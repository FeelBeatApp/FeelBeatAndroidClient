package com.github.feelbeatapp.androidclient

import com.github.feelbeatapp.androidclient.auth.OauthConfig

// TODO: Use gradle properties??

val spotifyOAuthConfig =
    OauthConfig(
        clientId = "0368b2bddb504887b517fc4e8fca9cc5",
        redirectUri = "feelbeat://callback",
        scope = "user-read-private user-read-email",
        authorizeUri = "https://accounts.spotify.com/authorize",
        tokenUri = "https://accounts.spotify.com/api/token",
        refreshUri = "https://accounts.spotify.com/api/token",
    )

object SocketConfig {
    const val SOCKET_URL = "ws://10.0.2.2:3000"
}
