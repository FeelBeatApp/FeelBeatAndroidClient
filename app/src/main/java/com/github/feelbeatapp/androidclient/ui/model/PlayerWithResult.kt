package com.github.feelbeatapp.androidclient.ui.model

import com.github.feelbeatapp.androidclient.ui.guesssong.ResultStatus

data class PlayerWithResult(val player: Player, val resultStatus: ResultStatus, val points: Int)
