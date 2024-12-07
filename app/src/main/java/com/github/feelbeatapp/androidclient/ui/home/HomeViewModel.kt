package com.github.feelbeatapp.androidclient.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//TODO doac graczy, playliste, ustawienia
data class Room(val id: Int, val name: String)

class HomeViewModel : ViewModel() {
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms.asStateFlow()

    private val _selectedRoom= MutableStateFlow<Room?>(null)
    val selectedRoom: StateFlow<Room?> = _selectedRoom.asStateFlow()

    init {
        loadRooms()
    }

    private fun loadRooms() {
        viewModelScope.launch {
            val exampleRooms = listOf(
                Room(1, "Pokoj 1"),
                Room(2, "Pokoj 2"),
                Room(3, "Pokoj 3"),
                Room(4, "Pokoj 4"),
                Room(5, "Pokoj 5"),
                Room(6, "Pokoj 6"),
                Room(7, "Pokoj 7")
            )
            _rooms.value = exampleRooms
        }
    }

    fun selectRoom(room: Room) {
        _selectedRoom.value = room
    }

    fun addRoom(name: String) {
        val newRoom = Room(id = _rooms.value.size + 1, name = name)
        _rooms.value = _rooms.value + newRoom
    }
}