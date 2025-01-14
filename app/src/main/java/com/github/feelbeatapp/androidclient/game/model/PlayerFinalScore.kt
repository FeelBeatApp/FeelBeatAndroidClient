package com.github.feelbeatapp.androidclient.game.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerFinalScore(
    val profile: Player ,
    val points: Int
)
