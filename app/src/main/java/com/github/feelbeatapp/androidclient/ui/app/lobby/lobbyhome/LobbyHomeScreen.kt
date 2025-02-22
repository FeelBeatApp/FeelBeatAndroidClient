package com.github.feelbeatapp.androidclient.ui.app.lobby.lobbyhome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.components.PlayerCard
import com.github.feelbeatapp.androidclient.ui.loading.LoadingScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LobbyHomeScreen(viewModel: LobbyHomeViewModel = hiltViewModel()) {
    val lobbyState by viewModel.lobbyHomeState.collectAsStateWithLifecycle()

    if (lobbyState.currentRoomId == null) {
        LoadingScreen()
        return
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(top = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.height(200.dp).width(150.dp),
        ) {
            AsyncImage(
                model = lobbyState.playlistImageUrl,
                contentDescription = stringResource(R.string.playlist_image),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.account),
                error = painterResource(R.drawable.account),
                modifier = Modifier.fillMaxSize(),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = lobbyState.playlistName,
                style = MaterialTheme.typography.displayMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Star, contentDescription = "star icon")
                Text(text = lobbyState.adminName, style = MaterialTheme.typography.titleLarge)
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            lobbyState.players.forEach { player ->
                PlayerCard(player = player) {
                    if (lobbyState.readyMap.getOrDefault(player.id, false))
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier =
                                Modifier.fillMaxSize()
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = .8f)),
                        ) {
                            Text("Ready!", color = MaterialTheme.colorScheme.onPrimary)
                        }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val ready = lobbyState.readyMap[lobbyState.me] ?: false
        Button(
            onClick = { viewModel.setReadyToPlay(!ready) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Text(
                text =
                    if (!ready) stringResource(R.string.ready)
                    else
                        stringResource(
                            R.string.waiting_for,
                            lobbyState.readyMap.count { it.value },
                            lobbyState.players.size,
                        ),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAcceptScreen() {
    LobbyHomeScreen()
}
