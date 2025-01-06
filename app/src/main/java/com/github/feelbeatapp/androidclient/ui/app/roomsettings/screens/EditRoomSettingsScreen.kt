package com.github.feelbeatapp.androidclient.ui.app.roomsettings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.components.SettingsControls
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels.EditRoomSettingsViewModel
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels.RoomSettingsViewModel

@Composable
fun EditRoomSettingsScreen(
    viewModel: RoomSettingsViewModel = EditRoomSettingsViewModel(),
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SettingsControls(viewModel)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewRoomSettingsScreen() {
    EditRoomSettingsScreen()
}
