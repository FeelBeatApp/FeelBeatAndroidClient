package com.github.feelbeatapp.androidclient.utils

import com.github.feelbeatapp.androidclient.infra.network.NetworkClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeNetworkAgent : NetworkClient {
    private val _sentMessages: MutableList<String> = ArrayList()
    val sentMessages: List<String>
        get() = _sentMessages.toList()

    val incoming = MutableSharedFlow<String>()

    suspend fun fakeMessageFromServer(text: String) {
        incoming.emit(text)
    }

    override suspend fun connect(path: String): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(text: String) {
        _sentMessages.add(text)
    }
}
