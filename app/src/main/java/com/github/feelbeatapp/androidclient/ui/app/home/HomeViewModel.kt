package com.github.feelbeatapp.androidclient.ui.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.infra.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.infra.error.FeelBeatException
import com.github.feelbeatapp.androidclient.api.feelbeat.responses.RoomListViewResponse
import com.github.feelbeatapp.androidclient.api.feelbeat.FeelBeatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(private val feelBeatApi: FeelBeatApi, private val errorReceiver: ErrorReceiver) :
    ViewModel() {
    private val _rooms = MutableStateFlow<List<RoomListViewResponse>>(listOf())
    val rooms: StateFlow<List<RoomListViewResponse>> = _rooms.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    @SuppressWarnings("MagicNumber")
    fun loadRooms() {
        _loading.value = true
        viewModelScope.launch {
            val roomsFromServer =
                try {
                    feelBeatApi.fetchRooms()
                } catch (e: FeelBeatException) {
                    errorReceiver.submitError(e)
                    listOf()
                }

            _rooms.value = roomsFromServer
            _loading.value = false
        }
    }
}
