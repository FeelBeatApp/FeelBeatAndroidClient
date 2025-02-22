package com.github.feelbeatapp.androidclient.ui.app

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.api.spotify.SpotifyAPI
import com.github.feelbeatapp.androidclient.game.datastreaming.GameDataStreamer
import com.github.feelbeatapp.androidclient.infra.auth.AuthManager
import com.github.feelbeatapp.androidclient.infra.error.ErrorEmitter
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatServerException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerIdentity(val name: String, val imageUrl: String)

@HiltViewModel
class AppViewModel
@Inject
constructor(
    @ApplicationContext private val ctx: Context,
    private val authManager: AuthManager,
    private val spotifyAPI: SpotifyAPI,
    private val errorEmitter: ErrorEmitter,
    private val gameDataStreamer: GameDataStreamer,
) : ViewModel() {
    private val _playerIdentity = MutableStateFlow<PlayerIdentity?>(null)
    val playerIdentity = _playerIdentity.asStateFlow()

    val snackBarHost = SnackbarHostState()

    init {
        loadPlayerIdentity()
        handleErrors()
    }

    private fun handleErrors() {
        viewModelScope.launch(Dispatchers.Default) {
            errorEmitter.errors.collect {
                snackBarHost.showSnackbar(formatException(it), withDismissAction = true)
            }
        }
    }

    private fun formatException(exception: FeelBeatException): String {
        return when (exception) {
            is FeelBeatServerException ->
                ctx.getString(exception.code.toStringId(), exception.serverMessage)
            else -> ctx.getString(exception.code.toStringId())
        }
    }

    private fun loadPlayerIdentity() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = spotifyAPI.getProfile()
            _playerIdentity.value =
                PlayerIdentity(
                    name = profile.displayName,
                    imageUrl = if (profile.images.isNotEmpty()) profile.images.first().url else "",
                )
        }
    }

    fun logout() {
        authManager.logout()
        gameDataStreamer.leaveRoom()
    }
}
