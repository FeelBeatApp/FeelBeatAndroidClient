package com.github.feelbeatapp.androidclient.ui.gameResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.guessSong.PlayerWithResult
import com.github.feelbeatapp.androidclient.ui.guessSong.ResultStatus
import com.github.feelbeatapp.androidclient.ui.startGame.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameResultViewModel : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerWithResult>>(emptyList())
    val players: StateFlow<List<PlayerWithResult>> = _players

    init {
        fetchGameResults()
    }

    private fun fetchGameResults() {
        viewModelScope.launch {
            val results =
                listOf(
                        PlayerWithResult(
                            Player("User123", R.drawable.userimage),
                            ResultStatus.CORRECT,
                            10,
                        ),
                        PlayerWithResult(
                            Player("User456", R.drawable.userimage),
                            ResultStatus.WRONG,
                            7,
                        ),
                        PlayerWithResult(
                            Player("User789", R.drawable.userimage),
                            ResultStatus.NORESPONSE,
                            1,
                        ),
                    )
                    .sortedByDescending { it.points }

            _players.value = results
        }
    }
}
