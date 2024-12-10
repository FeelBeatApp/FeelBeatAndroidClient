package com.github.feelbeatapp.androidclient.ui.home

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.network.spotify.SpotifyAPI
import com.github.feelbeatapp.androidclient.network.spotify.responses.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val spotifyAPI: SpotifyAPI) : ViewModel() {
    private val _profile = MutableStateFlow<ProfileResponse?>(null)
    val profile = _profile.asStateFlow()

    fun triggerProfileLoading() {
        if (profile.value != null) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val profileResponse = spotifyAPI.getProfile()
            _profile.update { profileResponse }
        }
    }
}
