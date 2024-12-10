package com.github.feelbeatapp.androidclient.di

import com.github.feelbeatapp.androidclient.BuildConfig
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.auth.OauthConfig
import com.github.feelbeatapp.androidclient.auth.spotify.SpotifyAuthManager
import com.github.feelbeatapp.androidclient.auth.storage.AuthStorage
import com.github.feelbeatapp.androidclient.auth.storage.PreferencesAuthStorage
import com.github.feelbeatapp.androidclient.network.fullduplex.NetworkAgent
import com.github.feelbeatapp.androidclient.network.fullduplex.WebsocketClient
import com.github.feelbeatapp.androidclient.network.spotify.KtorSpotifyAPI
import com.github.feelbeatapp.androidclient.network.spotify.SpotifyAPI
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object {
        private val httpClient =
            HttpClient(CIO) {
                install(WebSockets)
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            }

        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient {
            return httpClient
        }

        @Provides
        @Singleton
        fun provideNetworkAgent(): NetworkAgent {
            return WebsocketClient(httpClient, Url(BuildConfig.SOCKET_URI))
        }

        @Provides
        @Singleton
        fun provideOAuthConfig(): OauthConfig {
            return OauthConfig(
                clientId = BuildConfig.SPOTIFY_CLIENT_ID,
                redirectUri = BuildConfig.SPOTIFY_REDIRECT_URI,
                scope = BuildConfig.SPOTIFY_SCOPE,
                authorizeUri = BuildConfig.SPOTIFY_AUTHORIZE_URI,
                tokenUri = BuildConfig.SPOTIFY_TOKEN_URI,
                refreshUri = BuildConfig.SPOTIFY_REFRESH_URI,
            )
        }
    }

    @Singleton @Binds abstract fun bindAuthManager(authManager: SpotifyAuthManager): AuthManager

    @Singleton @Binds abstract fun bindAuthStorage(authStorage: PreferencesAuthStorage): AuthStorage

    @Singleton @Binds abstract fun bindSpotifyAPI(spotifyAPI: KtorSpotifyAPI): SpotifyAPI
}
