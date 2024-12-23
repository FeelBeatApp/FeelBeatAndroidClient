package com.github.feelbeatapp.androidclient.ui.newRoomSettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRoomSettingsScreen(
    viewModel: NewRoomSettingsViewModel = NewRoomSettingsViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val playlistLink by viewModel.playlistLink.collectAsState()

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
                    value = playlistLink,
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

@Composable
fun SettingSliders(viewModel: NewRoomSettingsViewModel) {
    val maxPlayers by viewModel.maxPlayers.collectAsState()
    val snippetDuration by viewModel.snippetDuration.collectAsState()
    val pointsToWin by viewModel.pointsToWin.collectAsState()

    SettingSlider(
        label = stringResource(R.string.number_of_players),
        value = maxPlayers,
        onValueChange = { viewModel.setMaxPlayers(it.toInt()) },
        valueRange = 1..5,
        steps = 4,
    )

    SettingSlider(
        label = stringResource(R.string.snippet_duration),
        value = snippetDuration,
        onValueChange = { viewModel.setSnippetDuration(it.toInt()) },
        valueRange = 5..30,
        steps = 5,
    )

    SettingSlider(
        label = stringResource(R.string.points_to_win),
        value = pointsToWin,
        onValueChange = { viewModel.setPointsToWin(it.toInt()) },
        valueRange = 3..10,
        steps = 6,
    )
}

@Composable
fun SettingSlider(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: IntRange,
    steps: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Slider(
                value = value.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
                steps = steps - 1,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = value.toString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewSettingsScreen() {
    val navController = rememberNavController()
    NewRoomSettingsScreen(navController = navController)
}
