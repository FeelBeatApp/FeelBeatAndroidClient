package com.github.feelbeatapp.androidclient.ui.state

import com.github.feelbeatapp.androidclient.ui.guessSong.ResultStatus

data class PlayerWithResult(val player: Player, val resultStatus: ResultStatus, val points: Int)
