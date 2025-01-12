package com.github.feelbeatapp.androidclient.ui.app.game.startgame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.components.PlayerCard
import com.github.feelbeatapp.androidclient.ui.app.navigation.AppRoute

@Composable
fun StartGameScreen(
    roomId: String,
    onNavigate: (String) -> Unit,
    viewModel: StartGameViewModel = hiltViewModel(),
) {
    val startGameState by viewModel.startGameState.collectAsStateWithLifecycle()

    LaunchedEffect(startGameState.counter) {
        if (startGameState.counter == 0) {
            onNavigate(AppRoute.GUESS.withArgs(mapOf("roomId" to roomId)))
        }
    }

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(stringResource(R.string.get_ready), style = MaterialTheme.typography.headlineLarge)
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            startGameState.players.forEach { player -> PlayerCard(player = player) }
        }
        if (startGameState.loading) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 4.dp,
                    modifier = Modifier.width(50.dp).height(50.dp),
                )
            }
        } else {
            Text(
                text = startGameState.counter.toString(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 32.dp),
            )
        }
    }
}

@Preview
@Composable
fun StartGamePreview() {
    StartGameScreen("roomId", {})
}
