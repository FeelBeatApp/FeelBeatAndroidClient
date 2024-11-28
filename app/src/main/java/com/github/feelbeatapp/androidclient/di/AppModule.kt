package com.github.feelbeatapp.androidclient.di

import com.github.feelbeatapp.androidclient.network.NetworkAgent
import com.github.feelbeatapp.androidclient.websocket.WebsocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNetworkAgent(): NetworkAgent {
        val wsClient = WebsocketClient(HttpClient(CIO) { install(WebSockets) })
        wsClient.connect(host = "10.0.2.2", port = 3000, path = "/ws")

        return wsClient
    }
}
