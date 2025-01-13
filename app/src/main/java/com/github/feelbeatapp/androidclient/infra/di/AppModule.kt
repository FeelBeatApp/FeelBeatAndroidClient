package com.github.feelbeatapp.androidclient.infra.di

import com.github.feelbeatapp.androidclient.BuildConfig
import com.github.feelbeatapp.androidclient.api.feelbeat.FeelBeatApi
import com.github.feelbeatapp.androidclient.api.feelbeat.KtorFeelBeatApi
import com.github.feelbeatapp.androidclient.api.spotify.KtorSpotifyAPI
import com.github.feelbeatapp.androidclient.api.spotify.SpotifyAPI
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.game.datastreaming.RemoteGameDataStreamer
import com.github.feelbeatapp.androidclient.infra.auth.AuthManager
import com.github.feelbeatapp.androidclient.infra.auth.OauthConfig
import com.github.feelbeatapp.androidclient.infra.auth.spotify.SpotifyAuthManager
import com.github.feelbeatapp.androidclient.infra.auth.storage.AuthStorage
import com.github.feelbeatapp.androidclient.infra.auth.storage.PreferencesAuthStorage
import com.github.feelbeatapp.androidclient.infra.error.ErrorEmitter
import com.github.feelbeatapp.androidclient.infra.error.ErrorHandler
import com.github.feelbeatapp.androidclient.infra.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.infra.network.NetworkClient
import com.github.feelbeatapp.androidclient.infra.network.WebsocketClient
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
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object {
        private val httpClient =
            HttpClient(CIO) {
                install(WebSockets)
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            }

        private val errorHandler = ErrorHandler()

        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient {
            return httpClient
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

        @Provides
        @Singleton
        @Named("API_URL")
        fun provideApiUrl(): String {
            return BuildConfig.API_URL
        }

        @Provides
        @Singleton
        @Named("SOCKET_URI")
        fun provideSocketUri(): Url {
            return Url(BuildConfig.SOCKET_URI)
        }

        @Provides
        @Singleton
        fun provideErrorEmitter(): ErrorEmitter {
            return errorHandler
        }

        @Provides
        @Singleton
        fun provideErrorReceiver(): ErrorReceiver {
            return errorHandler
        }
    }

    @Singleton @Binds abstract fun bindAuthManager(authManager: SpotifyAuthManager): AuthManager

    @Singleton @Binds abstract fun bindAuthStorage(authStorage: PreferencesAuthStorage): AuthStorage

    @Singleton @Binds abstract fun bindSpotifyAPI(spotifyAPI: KtorSpotifyAPI): SpotifyAPI

    @Singleton @Binds abstract fun bindFeelBeatAPI(feelBeatApi: KtorFeelBeatApi): FeelBeatApi

    @Singleton @Binds abstract fun bindNetworkClient(networkClient: WebsocketClient): NetworkClient

    @Singleton
    @Binds
    abstract fun bindGameDataStreamer(gameDataStreamer: RemoteGameDataStreamer): GameDataStreamer
}
