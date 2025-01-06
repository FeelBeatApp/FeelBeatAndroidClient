package com.github.feelbeatapp.androidclient.ui.app.game.gameresult

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.model.PlayerWithResult
import com.github.feelbeatapp.androidclient.ui.app.navigation.AppRoute

@Composable
fun GameResultScreen(
    onNavigate: (String) -> Unit,
    viewModel: GameResultViewModel = GameResultViewModel(),
) {
    val players by viewModel.players.collectAsState()

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
            items(players) { player -> PlayerScoreItem(player = player) }
        }

        Button(
            onClick = { onNavigate(AppRoute.HOME.name) },
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Text(text = "CLOSE")
        }
    }
}

@Composable
fun PlayerScoreItem(player: PlayerWithResult) {
    Box(modifier = Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.userimage),
                contentDescription = "Player Avatar",
                modifier = Modifier.size(48.dp).clip(CircleShape),
            )
            Text(
                text = "${player.player.name}: ${player.points} points",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
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
