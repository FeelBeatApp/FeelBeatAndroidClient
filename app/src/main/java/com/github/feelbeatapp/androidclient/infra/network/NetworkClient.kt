package com.github.feelbeatapp.androidclient.infra.network

import kotlinx.coroutines.flow.Flow

/** Implementation of communication with FeelBeat server */
interface NetworkClient {
    /**
     * Connects to FeelBeatServer
     *
     * @return flow of text messages from server
     */
    suspend fun connect(path: String): Flow<String>

    /** Disconnects current connection */
    suspend fun disconnect()

    /**
     * Sends message to FeelBeat server
     *
     * @param text text to send
     */
    suspend fun sendMessage(text: String)
}
