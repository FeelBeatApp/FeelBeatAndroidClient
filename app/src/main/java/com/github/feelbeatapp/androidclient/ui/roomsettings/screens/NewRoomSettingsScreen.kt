package com.github.feelbeatapp.androidclient.ui.roomsettings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import com.github.feelbeatapp.androidclient.ui.roomsettings.components.SettingSliders
import com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels.NewRoomSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRoomSettingsScreen(
    viewModel: NewRoomSettingsViewModel = NewRoomSettingsViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val roomSettings by viewModel.roomSettings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_room)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        },
        content = { padding ->
            Column(
                modifier = modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                SettingSliders(viewModel = viewModel)

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

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) {
                    Text(stringResource(R.string.create_room))
                }
            }
        },
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewSettingsScreen() {
    val navController = rememberNavController()
    NewRoomSettingsScreen(navController = navController)
}
