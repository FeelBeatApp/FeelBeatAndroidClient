package com.github.feelbeatapp.androidclient.auth

import android.content.Context

interface AuthManager {
    fun authState(): AuthState

    fun isAuthenticated(): Boolean {
        return authState() == AuthState.AUTHENTICATED
    }

    fun startLoginFlow(ctx: Context)

    suspend fun fetchAccessToken(code: String)

    suspend fun getAccessToken(): String
}
