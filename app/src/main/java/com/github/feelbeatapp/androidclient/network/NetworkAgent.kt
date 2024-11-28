package com.github.feelbeatapp.androidclient.network

import kotlinx.coroutines.flow.SharedFlow

/** Implementation of communication with FeelBeat server */
interface NetworkAgent {
    /** Connects to FeelBeatServer */
    fun connect(path: String)

    /** Disconnects current connection */
    suspend fun disconnect()

    /**
     * Sends message to FeelBeat server
     *
     * @param text text to send
     */
    suspend fun sendMessage(text: String)

    /** Received messages from FeelBeat server */
    fun receiveFlow(): SharedFlow<String>
}
