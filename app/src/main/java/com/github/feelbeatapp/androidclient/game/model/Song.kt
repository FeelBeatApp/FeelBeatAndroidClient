package com.github.feelbeatapp.androidclient.game.model

import kotlin.time.Duration

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val duration: Duration,
)
