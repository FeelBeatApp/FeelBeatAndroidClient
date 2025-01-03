package com.github.feelbeatapp.androidclient.ui.gameresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.model.Player
import com.github.feelbeatapp.androidclient.ui.model.PlayerWithResult
import com.github.feelbeatapp.androidclient.ui.guesssong.ResultStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameResultViewModel : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerWithResult>>(emptyList())
    val players: StateFlow<List<PlayerWithResult>> = _players

    init {
        fetchGameResults()
    }

    @SuppressWarnings("MagicNumber")
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
