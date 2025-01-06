package com.github.feelbeatapp.androidclient.error

import kotlinx.coroutines.flow.SharedFlow

interface ErrorEmitter {
    val errors: SharedFlow<FeelBeatException>
}
