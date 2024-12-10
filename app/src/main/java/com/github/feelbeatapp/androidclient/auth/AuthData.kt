package com.github.feelbeatapp.androidclient.auth

import java.time.Instant

data class AuthData(val accessToken: String, val refreshToken: String, val expires: Instant)
