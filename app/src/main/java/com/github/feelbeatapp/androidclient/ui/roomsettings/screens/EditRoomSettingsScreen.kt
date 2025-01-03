package com.github.feelbeatapp.androidclient.ui.roomsettings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute
import com.github.feelbeatapp.androidclient.ui.roomsettings.components.SettingSliders
import com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels.EditRoomSettingsViewModel
import com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels.RoomSettingsViewModel

@Composable
fun EditRoomSettingsScreen(
    viewModel: RoomSettingsViewModel = EditRoomSettingsViewModel(),
    internalNavController: NavController,
    modifier: Modifier = Modifier,
    isRoomCreator: Boolean = true,
) {
    val roomSettings by viewModel.roomSettings.collectAsState()

    Scaffold(
        bottomBar = {
            if (isRoomCreator) {
                BottomNavigationBar(navController = internalNavController)
            }
        },
        content = { padding ->
            Column(
                modifier = modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                SettingSliders(viewModel)

                Text(
                    text = stringResource(R.string.playlist_link),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )

                TextField(
                    value = roomSettings.playlistLink,
                    onValueChange = { viewModel.setPlaylistLink(it) },
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
                    label = { Text(stringResource(R.string.enter_playlist_link)) },
                )
            }
        },
    )
}

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        NavigationBarItem(
            icon = {
                Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.selected_room))
            },
            label = { Text(stringResource(R.string.selected_room)) },
            selected = false,
            onClick = { navController.navigate(FeelBeatRoute.ACCEPT_GAME.name) },
        )
        NavigationBarItem(
            icon = {
                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
            },
            label = { Text(stringResource(R.string.settings)) },
            selected = false,
            onClick = { navController.navigate(FeelBeatRoute.ROOM_SETTINGS.name) },
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewRoomSettingsScreen() {
    val navController = rememberNavController()
    EditRoomSettingsScreen(internalNavController = navController)
}
