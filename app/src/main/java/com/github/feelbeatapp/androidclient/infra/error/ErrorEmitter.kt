package com.github.feelbeatapp.androidclient.infra.error

import kotlinx.coroutines.flow.SharedFlow

interface ErrorEmitter {
    val errors: SharedFlow<FeelBeatException>
}
