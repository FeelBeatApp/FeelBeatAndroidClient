package com.github.feelbeatapp.androidclient.ui.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.error.ErrorReceiver
import com.github.feelbeatapp.androidclient.error.FeelBeatException
import com.github.feelbeatapp.androidclient.model.RoomListView
import com.github.feelbeatapp.androidclient.network.api.FeelBeatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(private val feelBeatApi: FeelBeatApi, private val errorReceiver: ErrorReceiver) :
    ViewModel() {
    private val _rooms = MutableStateFlow<List<RoomListView>>(listOf())
    val rooms: StateFlow<List<RoomListView>> = _rooms.asStateFlow()

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
