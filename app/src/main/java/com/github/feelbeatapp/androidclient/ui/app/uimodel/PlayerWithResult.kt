package com.github.feelbeatapp.androidclient.ui.app.uimodel

import com.github.feelbeatapp.androidclient.game.model.Player
import com.github.feelbeatapp.androidclient.ui.app.game.guesssong.ResultStatus

data class PlayerWithResult(val player: Player, val resultStatus: ResultStatus, val points: Int)
