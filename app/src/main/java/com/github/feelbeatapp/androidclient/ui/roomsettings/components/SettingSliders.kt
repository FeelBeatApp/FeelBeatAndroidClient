package com.github.feelbeatapp.androidclient.ui.roomsettings.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.roomsettings.viewmodels.RoomSettingsViewModel

@Composable
fun SettingSliders(viewModel: RoomSettingsViewModel) {
    val roomSettings by viewModel.roomSettings.collectAsState()

    SettingSlider(
        label = stringResource(R.string.number_of_players),
        value = roomSettings.maxPlayers,
        onValueChange = { viewModel.setMaxPlayers(it.toInt()) },
        valueRange = 1..5,
        steps = 4,
    )

    SettingSlider(
        label = stringResource(R.string.snippet_duration),
        value = roomSettings.snippetDuration,
        onValueChange = { viewModel.setSnippetDuration(it.toInt()) },
        valueRange = 5..30,
        steps = 5,
    )

    SettingSlider(
        label = stringResource(R.string.points_to_win),
        value = roomSettings.pointsToWin,
        onValueChange = { viewModel.setPointsToWin(it.toInt()) },
        valueRange = 3..10,
        steps = 6,
    )
}
