package com.github.feelbeatapp.androidclient.ui.app.roomsettings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.components.SettingsControls
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels.LobbyRoomSettingsViewModel
import com.github.feelbeatapp.androidclient.ui.loading.LoadingScreen

@Composable
fun LobbyRoomSettingsScreen(
    viewModel: LobbyRoomSettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val editRoomState by viewModel.editRoomState.collectAsStateWithLifecycle()

    if (!editRoomState.loaded) {
        LoadingScreen()
        return
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SettingsControls(viewModel, enabled = editRoomState.isAdmin)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewRoomSettingsScreen() {
    LobbyRoomSettingsScreen()
}
