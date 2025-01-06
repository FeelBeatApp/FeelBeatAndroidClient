package com.github.feelbeatapp.androidclient.error

interface ErrorReceiver {
    suspend fun submitError(exception: FeelBeatException)
}
