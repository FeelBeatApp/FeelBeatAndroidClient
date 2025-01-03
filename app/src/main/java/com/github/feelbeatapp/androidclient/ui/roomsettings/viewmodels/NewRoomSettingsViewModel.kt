package com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels

import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.network.api.FeelBeatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoomCreationState(val errorMessage: String?, val loading: Boolean = false)

@HiltViewModel
class NewRoomSettingsViewModel @Inject constructor(private val feelBeatApi: FeelBeatApi) :
    RoomSettingsViewModel() {
    private val _roomCreationState = MutableStateFlow<RoomCreationState>(RoomCreationState(null))
    val roomCreationState = _roomCreationState.asStateFlow()

    private val _roomCreated = MutableSharedFlow<String?>()
    val roomCreated = _roomCreated.asSharedFlow()

    fun createRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            _roomCreationState.update { it.copy(loading = true) }
            try {
                val roomId = feelBeatApi.createRoom(roomSettings.value)
                _roomCreated.emit(roomId)
                _roomCreationState.update { it.copy(loading = false) }
            } catch (e: Throwable) {
                _roomCreationState.update { it.copy(errorMessage = e.message, loading = false) }
            }
        }
    }
}
