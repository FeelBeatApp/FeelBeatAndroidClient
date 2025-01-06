package com.github.feelbeatapp.androidclient.di

import androidx.compose.material3.SnackbarHostState
import com.github.feelbeatapp.androidclient.BuildConfig
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.auth.OauthConfig
import com.github.feelbeatapp.androidclient.auth.spotify.SpotifyAuthManager
import com.github.feelbeatapp.androidclient.auth.storage.AuthStorage
import com.github.feelbeatapp.androidclient.auth.storage.PreferencesAuthStorage
import com.github.feelbeatapp.androidclient.error.ErrorEmitter
import com.github.feelbeatapp.androidclient.error.ErrorHandler
import com.github.feelbeatapp.androidclient.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.network.api.FeelBeatApi
import com.github.feelbeatapp.androidclient.network.api.KtorFeelBeatApi
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
import javax.inject.Named
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

        private val errorHandler = ErrorHandler()

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

        @Provides
        @Singleton
        @Named("API_URL")
        fun provideApiUrl(): String {
            return BuildConfig.API_URL
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
}
