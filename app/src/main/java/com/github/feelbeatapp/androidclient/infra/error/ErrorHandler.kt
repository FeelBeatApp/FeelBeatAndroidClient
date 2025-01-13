package com.github.feelbeatapp.androidclient.infra.error

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ErrorHandler @Inject constructor() : ErrorEmitter, ErrorReceiver {
    private val _errors = MutableSharedFlow<FeelBeatException>(extraBufferCapacity = 32)
    override val errors = _errors.asSharedFlow()

    override suspend fun submitError(exception: FeelBeatException) {
        Log.e("App error", exception.toString())
        _errors.emit(exception)
    }
}
