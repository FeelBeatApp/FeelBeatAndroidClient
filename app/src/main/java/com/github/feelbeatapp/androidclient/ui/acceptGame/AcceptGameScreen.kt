package com.github.feelbeatapp.androidclient.ui.acceptGame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute
import com.github.feelbeatapp.androidclient.ui.state.Song
import com.github.feelbeatapp.androidclient.ui.startGame.PlayerCard

@Composable
fun AcceptGameScreen(
    viewModel: AcceptGameViewModel = AcceptGameViewModel(),
    navController: NavController,
    isRoomCreator: Boolean = true,
) {
    val gameState = viewModel.gameState.collectAsState().value

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(bottom = 56.dp)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = { navController.navigate(FeelBeatRoute.ACCEPT_GAME.name) }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.back),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                gameState.players.forEach { player -> PlayerCard(player = player) }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Text(
                    text =
                        stringResource(
                            id = R.string.snippet_duration_val,
                            gameState.snippetDuration,
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = stringResource(id = R.string.points_to_win_val, gameState.pointsToWin),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.playlist_name, gameState.playlist.name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            gameState.playlist.songs.forEach { song -> SongItem(song = song) }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(FeelBeatRoute.START_GAME.name) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            ) {
                Text(stringResource(R.string.play), style = MaterialTheme.typography.headlineMedium)
            }
        }

        if (isRoomCreator) {
            BottomNavigationBar(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
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

@Composable
fun SongItem(song: Song) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
fun PreviewAcceptScreen() {
    val navController = rememberNavController()
    AcceptGameScreen(navController = navController)
}
