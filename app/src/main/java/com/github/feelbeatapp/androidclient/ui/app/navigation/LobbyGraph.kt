package com.github.feelbeatapp.androidclient.ui.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.feelbeatapp.androidclient.ui.app.lobby.lobbyhome.LobbyHomeScreen
import com.github.feelbeatapp.androidclient.ui.app.lobby.lobbysongs.LobbySongsScreen
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.screens.LobbyRoomSettingsScreen

fun NavGraphBuilder.lobbyGraph() {
    composable(route = AppRoute.ROOM_LOBBY.route) { LobbyHomeScreen() }

    composable(route = AppRoute.ROOM_SONGS.route) { LobbySongsScreen() }

    composable(route = AppRoute.ROOM_EDIT.route) { LobbyRoomSettingsScreen() }
}
