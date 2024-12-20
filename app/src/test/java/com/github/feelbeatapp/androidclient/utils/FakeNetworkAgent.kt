package com.github.feelbeatapp.androidclient.utils

import com.github.feelbeatapp.androidclient.network.fullduplex.NetworkAgent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeNetworkAgent : NetworkAgent {
    private val _sentMessages: MutableList<String> = ArrayList()
    val sentMessages: List<String>
        get() = _sentMessages.toList()

    val incoming = MutableSharedFlow<String>()

    suspend fun fakeMessageFromServer(text: String) {
        incoming.emit(text)
    }

    override fun connect(path: String) {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(text: String) {
        _sentMessages.add(text)
    }

    override fun receiveFlow(): SharedFlow<String> {
        return incoming.asSharedFlow()
    }
}
