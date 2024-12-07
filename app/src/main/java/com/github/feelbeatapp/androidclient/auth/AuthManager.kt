package com.github.feelbeatapp.androidclient.auth

import android.content.Context

interface AuthManager {
    fun isAuthenticated(): Boolean

    fun startLoginFlow(ctx: Context)

    suspend fun fetchAccessToken(code: String)
}
