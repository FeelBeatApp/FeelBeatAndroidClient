package com.github.feelbeatapp.androidclient.ui.app.game.gameresult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.game.model.Player
import com.github.feelbeatapp.androidclient.ui.app.navigation.AppRoute

@Composable
fun GameResultScreen(
    onNavigate: (String) -> Unit,
    viewModel: GameResultViewModel = hiltViewModel(),
) {
    val gameResultState by viewModel.gameResultState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Game Results",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(gameResultState.results.sortedByDescending { it.points }) { result ->
                PlayerScoreItem(player = result.profile, score = result.points)
            }
        }

        Button(
            onClick = {
                onNavigate(AppRoute.ROOM_LOBBY.withArgs(mapOf("roomId" to gameResultState.roomId)))
            },
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Text(text = "CLOSE")
        }
    }
}

@Composable
fun PlayerScoreItem(player: Player, score: Int) {
    Box(modifier = Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = player.imageUrl,
                placeholder = painterResource(R.drawable.account),
                error = painterResource(R.drawable.account),
                contentDescription = stringResource(R.string.player_avatar),
                modifier = Modifier.size(48.dp).clip(CircleShape),
            )

            Text(
                text = "${player.name}: $score points",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
    }
}

@Preview
@Composable
fun GameResultPreview() {
    GameResultScreen({})
}
