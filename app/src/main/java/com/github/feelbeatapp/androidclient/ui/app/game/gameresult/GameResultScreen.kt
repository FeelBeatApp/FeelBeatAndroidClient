package com.github.feelbeatapp.androidclient.ui.app.game.gameresult

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GameResultScreen(
    onNavigate: (String) -> Unit,
    viewModel: GameResultViewModel = hiltViewModel(),
) {
    //    val players by viewModel.players.collectAsState()
    //
    //    Column(
    //        modifier = Modifier.fillMaxSize().padding(16.dp),
    //        verticalArrangement = Arrangement.SpaceBetween,
    //        horizontalAlignment = Alignment.CenterHorizontally,
    //    ) {
    //        Text(
    //            text = "Game Results",
    //            style = MaterialTheme.typography.headlineMedium,
    //            fontWeight = FontWeight.Bold,
    //        )
    //
    //        LazyColumn(
    //            modifier = Modifier.fillMaxWidth(),
    //            verticalArrangement = Arrangement.spacedBy(8.dp),
    //            contentPadding = PaddingValues(8.dp),
    //        ) {
    //            items(players) { player -> PlayerScoreItem(player = player) }
    //        }
    //
    //        Button(
    //            onClick = { onNavigate(AppRoute.HOME.name) },
    //            modifier = Modifier.padding(vertical = 16.dp),
    //        ) {
    //            Text(text = "CLOSE")
    //        }
    //    }
    // }
    //
    // @Composable
    // fun PlayerScoreItem(player: Player) {
    //    Box(modifier = Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center)
    // {
    //        Row(verticalAlignment = Alignment.CenterVertically, modifier =
    // Modifier.fillMaxWidth()) {
    //            AsyncImage(
    //                model = player.imageUrl,
    //                placeholder = painterResource(R.drawable.userimage),
    //                error = painterResource(R.drawable.userimage),
    //                contentDescription = stringResource(R.string.player_avatar),
    //                modifier = Modifier.size(48.dp).clip(CircleShape),
    //            )
    //
    //            Text(
    //                text = "${player.name}: ${player.score} points",
    //                style = MaterialTheme.typography.bodyLarge,
    //                fontWeight = FontWeight.Bold,
    //                color = Color.Black,
    //                modifier = Modifier.padding(start = 16.dp),
    //            )
    //        }
    //    }
}

@Preview
@Composable
fun GameResultPreview() {
    GameResultScreen({})
}
