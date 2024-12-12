package com.github.feelbeatapp.androidclient.auth.spotify

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import com.github.feelbeatapp.androidclient.auth.AuthData
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.auth.AuthState
import com.github.feelbeatapp.androidclient.auth.OauthConfig
import com.github.feelbeatapp.androidclient.auth.PKCEUtils
import com.github.feelbeatapp.androidclient.auth.storage.AuthStorage
import com.github.feelbeatapp.androidclient.error.ErrorCode
import com.github.feelbeatapp.androidclient.error.FeelBeatException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import java.io.IOException
import java.time.Instant
import javax.inject.Inject

class SpotifyAuthManager
@Inject
constructor(
    private val oauthConfig: OauthConfig,
    private val httpClient: HttpClient,
    private val authStorage: AuthStorage,
) : AuthManager {
    private val pkce = PKCEUtils()
    private var currentCodeVerifier: String? = null
    private var authData: AuthData? = null
    private var state: AuthState = AuthState.NOT_AUTHENTICATED

    init {
        authData = authStorage.retrieveAuthData()
        if (authData != null) {
            state = AuthState.AUTHENTICATED
        }
    }

    override fun authState(): AuthState {
        return state
    }

    override fun startLoginFlow(ctx: Context) {
        if (state != AuthState.NOT_AUTHENTICATED) {
            return
        }

        state = AuthState.AUTHENTICATING

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
        val response =
            try {
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
            } catch (e: Exception) {
                when (e) {
                    is IOException,
                    is UnresolvedAddressException ->
                        throw FeelBeatException(ErrorCode.AUTHORIZATION_SERVER_UNREACHABLE, e)
                    else -> throw e
                }
            }

        val tokenResponse =
            try {
                response.body<TokenResponse>()
            } catch (e: JsonConvertException) {
                throw FeelBeatException(ErrorCode.AUTHORIZATION_ACCESS_TOKEN_ERROR, e)
            }

        val newAuthData = tokenResponseToAuthData(tokenResponse)
        authStorage.storeAuthData(newAuthData)
        authData = newAuthData
        onAuthenticated()
    }

    override suspend fun getAccessToken(): String {
        var auth = checkNotNull(authData) { "Not authenticated" }
        if (hasExpired(auth.expires)) {
            refreshAccessToken()
            auth = checkNotNull(authData) { "Not authenticated" }
        }

        return auth.accessToken
    }

    private fun onAuthenticated() {
        state = AuthState.AUTHENTICATED
    }

    private fun calculateExpiration(expiresIn: Int): Instant {
        return Instant.now().plusSeconds(expiresIn.toLong())
    }

    private fun hasExpired(expires: Instant): Boolean {
        return expires >= Instant.now()
    }

    private suspend fun refreshAccessToken() {
        Log.i("SpotifyAuth", "refreshing access token")
        val auth = checkNotNull(authData, { "Not authenticated" })

        val response =
            try {
                httpClient.submitForm(
                    oauthConfig.refreshUri,
                    parameters {
                        append("grant_type", "refresh_token")
                        append("refresh_token", auth.refreshToken)
                        append("client_id", oauthConfig.clientId)
                    },
                ) {}
            } catch (e: Exception) {
                when (e) {
                    is IOException,
                    is UnresolvedAddressException ->
                        throw FeelBeatException(ErrorCode.AUTHORIZATION_SERVER_UNREACHABLE, e)

                    else -> throw e
                }
            }

        val tokenResponse =
            try {
                response.body<TokenResponse>()
            } catch (e: JsonConvertException) {
                throw FeelBeatException(ErrorCode.AUTHORIZATION_REFRESH_TOKEN_ERROR, e)
            }
        val newAuthData = tokenResponseToAuthData(tokenResponse)
        authStorage.storeAuthData(newAuthData)
        authData = newAuthData
        Log.i("SpotifyAuth", "access token refreshed successfully")
    }

    private fun tokenResponseToAuthData(tokenResponse: TokenResponse): AuthData {
        return AuthData(
            accessToken = tokenResponse.accessToken,
            refreshToken = tokenResponse.refreshToken,
            expires = calculateExpiration(tokenResponse.expiresIn),
        )
    }
}
