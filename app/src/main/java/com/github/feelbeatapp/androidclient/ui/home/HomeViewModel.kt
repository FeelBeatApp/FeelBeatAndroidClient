package com.github.feelbeatapp.androidclient.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.error.ErrorCode
import com.github.feelbeatapp.androidclient.error.FeelBeatException
import com.github.feelbeatapp.androidclient.network.spotify.SpotifyAPI
import com.github.feelbeatapp.androidclient.network.spotify.responses.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val spotifyAPI: SpotifyAPI) : ViewModel() {
    private val _profile = MutableStateFlow<ProfileResponse?>(null)
    private val _error = MutableStateFlow<ErrorCode?>(null)
    val profile = _profile.asStateFlow()
    val error = _error.asStateFlow()

    fun triggerProfileLoading() {
        if (profile.value != null) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val profileResponse = spotifyAPI.getProfile()
                _profile.update { profileResponse }
            } catch (e: FeelBeatException) {
                _error.update { e.code }
                Log.e("HomeViewModel", e.message.toString())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Unhandled exception: ${e.message}")
            }
        }
    }
}
