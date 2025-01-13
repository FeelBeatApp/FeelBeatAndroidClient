package com.github.feelbeatapp.androidclient.game.model

import java.time.Duration
import java.time.Instant

data class AudioState(val url: String, val startAt: Instant, val duration: Duration)
