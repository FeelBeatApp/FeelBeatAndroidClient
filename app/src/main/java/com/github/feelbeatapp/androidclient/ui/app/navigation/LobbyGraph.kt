package com.github.feelbeatapp.androidclient.ui.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.feelbeatapp.androidclient.ui.app.lobby.acceptgame.AcceptGameScreen
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.screens.EditRoomSettingsScreen


fun NavGraphBuilder.lobbyGraph(onPlay: () -> Unit) {
    composable(route = AppRoute.ROOM_LOBBY.route) {
        AcceptGameScreen(it.getRoomId(), onPlay = onPlay)
    }
    composable(route = AppRoute.ROOM_EDIT.route) { EditRoomSettingsScreen() }
}
