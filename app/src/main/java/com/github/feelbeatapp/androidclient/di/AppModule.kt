package com.github.feelbeatapp.androidclient.di

import com.github.feelbeatapp.androidclient.SocketConfig
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.auth.OauthConfig
import com.github.feelbeatapp.androidclient.auth.SpotifyAuthManager
import com.github.feelbeatapp.androidclient.network.NetworkAgent
import com.github.feelbeatapp.androidclient.network.websocket.WebsocketClient
import com.github.feelbeatapp.androidclient.spotifyOAuthConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.Url
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object {
        private val httpClient = HttpClient(CIO) { install(WebSockets) }

        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient {
            return httpClient
        }

        @Provides
        @Singleton
        fun provideNetworkAgent(): NetworkAgent {
            return WebsocketClient(httpClient, Url(SocketConfig.SOCKET_URL))
        }

        @Provides
        @Singleton
        fun provideOAuthConfig(): OauthConfig {
            return spotifyOAuthConfig
        }
    }

    @Singleton @Binds abstract fun bindAuthManager(authManager: SpotifyAuthManager): AuthManager
}
