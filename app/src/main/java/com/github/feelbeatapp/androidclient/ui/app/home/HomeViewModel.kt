package com.github.feelbeatapp.androidclient.ui.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.ui.app.model.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms.asStateFlow()

    init {
        loadRooms()
    }

    @SuppressWarnings("MagicNumber")
    private fun loadRooms() {
        viewModelScope.launch {
            val exampleRooms =
                listOf(
                    Room(1, "Pokój 1", 4, 30, 10, "https://example.com/playlist1"),
                    Room(2, "Pokój 2", 5, 20, 5, "https://example.com/playlist2"),
                    Room(3, "Pokój 3", 2, 25, 3, "https://example.com/playlist3"),
                    Room(4, "Pokój 4", 4, 30, 4, "https://example.com/playlist4"),
                    Room(5, "Pokój 5", 5, 25, 4, "https://example.com/playlist5"),
                    Room(6, "Pokój 6", 2, 25, 10, "https://example.com/playlist6"),
                    Room(7, "Pokój 7", 4, 30, 9, "https://example.com/playlist7"),
                )
            _rooms.value = exampleRooms
        }
    }
}
