package com.github.feelbeatapp.androidclient.di

import android.content.Context
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.network.NetworkAgent
import com.github.feelbeatapp.androidclient.websocket.WebsocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.Url
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNetworkAgent(@ApplicationContext context: Context): NetworkAgent {
        val wsClient =
            WebsocketClient(
                HttpClient(CIO) { install(WebSockets) },
                Url(context.getString(R.string.socket_url)),
            )

        return wsClient
    }
}
