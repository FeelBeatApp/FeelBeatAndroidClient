package com.github.feelbeatapp.androidclient.game.model

import kotlinx.serialization.Serializable

@Serializable data class Player(val id: String, val name: String, val imageUrl: String)
