package com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels

import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.infra.error.ErrorCode
import com.github.feelbeatapp.androidclient.infra.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import com.github.feelbeatapp.androidclient.api.feelbeat.FeelBeatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.Url
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val SPOTIFY_PLAYLIST_HOST = "open.spotify.com"

@HiltViewModel
class NewRoomSettingsViewModel
@Inject
constructor(private val feelBeatApi: FeelBeatApi, private val errorReceiver: ErrorReceiver) :
    RoomSettingsViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _roomCreated = MutableSharedFlow<String?>()
    val roomCreated = _roomCreated.asSharedFlow()

    fun createRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = Url(roomSettings.value.playlistLink)
            if (url.host != SPOTIFY_PLAYLIST_HOST || url.segments.isEmpty()) {
                errorReceiver.submitError(FeelBeatException(ErrorCode.INCORRECT_PLAYLIST_LINK))
                return@launch
            }

            _loading.value = true
            try {
                val roomId = feelBeatApi.createRoom(roomSettings.value.toCreateRoomPayload())
                _roomCreated.emit(roomId)
            } catch (e: FeelBeatException) {
                errorReceiver.submitError(e)
            } finally {
                _loading.value = false
            }
        }
    }
}
