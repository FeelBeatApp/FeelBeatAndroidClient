package com.github.feelbeatapp.androidclient.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.network.spotify.SpotifyAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerIdentity(val name: String, val imageUrl: String)

@HiltViewModel
class AppViewModel
@Inject
constructor(private val authManager: AuthManager, private val spotifyAPI: SpotifyAPI) :
    ViewModel() {
    private val _playerIdentity = MutableStateFlow<PlayerIdentity?>(null)
    val playerIdentity = _playerIdentity.asStateFlow()

    init {
        loadPlayerIdentity()
    }

    private fun loadPlayerIdentity() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = spotifyAPI.getProfile()
            _playerIdentity.value =
                PlayerIdentity(name = profile.displayName, imageUrl = profile.images.first().url)
        }
    }

    fun logout() {
        authManager.logout()
    }
}
