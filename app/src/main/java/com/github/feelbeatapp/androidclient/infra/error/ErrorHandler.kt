package com.github.feelbeatapp.androidclient.infra.error

import android.util.Log
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ErrorHandler @Inject constructor() : ErrorEmitter, ErrorReceiver {
    private val _errors = MutableSharedFlow<FeelBeatException>()
    override val errors = _errors.asSharedFlow()

    override suspend fun submitError(exception: FeelBeatException) {
        Log.e("App error", exception.toString())
        _errors.emit(exception)
    }
}
