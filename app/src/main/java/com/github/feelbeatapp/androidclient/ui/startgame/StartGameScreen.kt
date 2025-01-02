package com.github.feelbeatapp.androidclient.ui.startgame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute
import com.github.feelbeatapp.androidclient.model.Player

@Composable
fun StartGameScreen(
    viewModel: StartGameViewModel = StartGameViewModel(),
    navController: NavController,
) {
    val players by viewModel.players.collectAsState()
    var countdown by remember { mutableIntStateOf(value = 3) }

    LaunchedEffect(key1 = countdown) {
        if (countdown > 0) {
            kotlinx.coroutines.delay(timeMillis = 1000)
            countdown -= 1
        } else {
            navController.navigate(FeelBeatRoute.GUESS_SONG.name)
        }
    }

    Column(
        modifier =
            Modifier.fillMaxSize().padding(16.dp).background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            players.forEach { player -> PlayerCard(player = player) }
        }
        Text(
            text = countdown.toString(),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 32.dp),
        )
    }
}

@Composable
fun PlayerCard(player: Player) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Image(
            painter = painterResource(id = player.image),
            contentDescription = stringResource(R.string.player_image),
            modifier =
                Modifier.size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
        )
        Text(
            text = player.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@Preview
@Composable
fun StartGamePreview() {
    val navController = rememberNavController()
    StartGameScreen(navController = navController)
}
