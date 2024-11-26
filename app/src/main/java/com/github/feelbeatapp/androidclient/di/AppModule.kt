package com.github.feelbeatapp.androidclient.di

import com.github.feelbeatapp.androidclient.network.NetworkAgent
import com.github.feelbeatapp.androidclient.websocket.WebsocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNetworkAgent(): NetworkAgent {
        val wsClient = WebsocketClient()
        wsClient.connect(host = "10.0.2.2", port = 3000, path = "/ws")

        return wsClient
    }
}
