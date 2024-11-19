package com.github.feelbeatapp.androidclient.network

import kotlinx.coroutines.flow.SharedFlow

interface NetworkAgent {
    suspend fun sendMessage(text: String)
    fun incomingFlow(): SharedFlow<String>
}