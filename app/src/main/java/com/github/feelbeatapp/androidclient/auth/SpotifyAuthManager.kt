package com.github.feelbeatapp.androidclient.auth

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import javax.inject.Inject

class SpotifyAuthManager
@Inject
constructor(private val oauthConfig: OauthConfig, private val httpClient: HttpClient) :
    AuthManager {
    private val pkce = PKCEUtils()
    private var currentCodeVerifier: String? = null
    private var authState: AuthState? = null

    override fun isAuthenticated(): Boolean {
        return authState != null
    }

    override fun startLoginFlow(ctx: Context) {
        val loginIntent = CustomTabsIntent.Builder().build()

        val code = pkce.generateCodeVerifier()

        val authorizeRedirect =
            Uri.parse(oauthConfig.authorizeUri)
                .buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("code_challenge_method", "S256")
                .appendQueryParameter("client_id", oauthConfig.clientId)
                .appendQueryParameter("scope", oauthConfig.scope)
                .appendQueryParameter("redirect_uri", oauthConfig.redirectUri)
                .appendQueryParameter("code_challenge", pkce.getCodeChallenge(code))
                .build()

        currentCodeVerifier = code
        loginIntent.launchUrl(ctx, authorizeRedirect)
    }

    override suspend fun fetchAccessToken(code: String) {
        Log.d("here", "good")
        val response =
            httpClient.submitForm(
                oauthConfig.tokenUri,
                formParameters =
                    parameters {
                        append("grant_type", "authorization_code")
                        append("code", code)
                        append("redirect_uri", oauthConfig.redirectUri)
                        append("client_id", oauthConfig.clientId)
                        append("code_verifier", checkNotNull(currentCodeVerifier))
                    },
            ) {}
        authState = AuthState(accessToken = "hi", refreshToken = "hi")

        Log.d("re", response.toString())
        Log.d("response", response.body())
    }
}
