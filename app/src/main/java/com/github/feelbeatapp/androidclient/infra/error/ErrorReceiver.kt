package com.github.feelbeatapp.androidclient.infra.error

interface ErrorReceiver {
    suspend fun submitError(exception: FeelBeatException)
}
