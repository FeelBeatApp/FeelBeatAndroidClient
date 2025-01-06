package com.github.feelbeatapp.androidclient.ui.app.model

import com.github.feelbeatapp.androidclient.ui.app.game.guesssong.ResultStatus


data class PlayerWithResult(val player: Player, val resultStatus: ResultStatus, val points: Int)
