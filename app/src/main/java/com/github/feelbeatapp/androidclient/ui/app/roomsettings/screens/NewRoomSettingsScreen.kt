package com.github.feelbeatapp.androidclient.ui.app.roomsettings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.components.SettingsControls
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.viewmodels.NewRoomSettingsViewModel

@Composable
fun NewRoomSettingsScreen(
    onRoomCreated: (String) -> Unit,
    newRoomSettingsViewModel: NewRoomSettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val createdRoomId by newRoomSettingsViewModel.roomCreated.collectAsStateWithLifecycle(null)
    val loading by newRoomSettingsViewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(createdRoomId) {
        val roomId = createdRoomId
        if (roomId != null) {
            onRoomCreated(roomId)
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SettingsControls(viewModel = newRoomSettingsViewModel)

        if (loading) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 4.dp,
                    modifier = Modifier.width(50.dp).height(50.dp),
                )
            }
        } else if (createdRoomId == null) {
            Button(
                onClick = { newRoomSettingsViewModel.createRoom() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                Text(stringResource(R.string.create_room))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewSettingsScreen() {
    NewRoomSettingsScreen(onRoomCreated = {})
}
