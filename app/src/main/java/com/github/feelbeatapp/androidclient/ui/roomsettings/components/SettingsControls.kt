package com.github.feelbeatapp.androidclient.ui.roomsettings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels.RoomSettingsViewModel

@Composable
fun SettingsControls(viewModel: RoomSettingsViewModel) {
    val roomSettings by viewModel.roomSettings.collectAsState()

    SettingSlider(
        label = stringResource(R.string.number_of_players),
        value = roomSettings.maxPlayers,
        onValueChange = { viewModel.setMaxPlayers(it) },
        valueRange = 1..5,
        steps = 4,
    )

    SettingSlider(
        label = stringResource(R.string.turn_count),
        value = roomSettings.turnCount,
        onValueChange = { viewModel.setTurnCount(it) },
        valueRange = 1..10,
        steps = 9,
    )

    SettingSlider(
        label = stringResource(R.string.time_penalty_per_second),
        value = roomSettings.timePenaltyPerSecond,
        onValueChange = { viewModel.setTimePenaltyPerSecond(it) },
        valueRange = 1..20,
        steps = 19,
    )

    SettingSlider(
        label = stringResource(R.string.base_points),
        value = roomSettings.basePoints,
        onValueChange = { viewModel.setBasePoints(it) },
        valueRange = 100..1000,
        steps = 9,
    )

    SettingSlider(
        label = stringResource(R.string.incorrectGuessPenalty),
        value = roomSettings.incorrectGuessPenalty,
        onValueChange = { viewModel.setIncorrectGuessPenalty(it) },
        valueRange = 50..500,
        steps = 9,
    )

    Text(
        text = stringResource(R.string.playlist_link),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 8.dp),
    )

    TextField(
        value = roomSettings.playlistLink,
        keyboardOptions =
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done,
            ),
        onValueChange = { viewModel.setPlaylistLink(it) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        label = { Text(stringResource(R.string.enter_playlist_link)) },
    )
}
