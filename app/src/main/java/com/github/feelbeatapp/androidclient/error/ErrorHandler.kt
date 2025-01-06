package com.github.feelbeatapp.androidclient.error

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ErrorHandler @Inject constructor() : ErrorEmitter, ErrorReceiver {
    private val _errors = MutableSharedFlow<FeelBeatException>()
    override val errors = _errors.asSharedFlow()

    override suspend fun submitError(exception: FeelBeatException) {
        _errors.emit(exception)
    }
}
