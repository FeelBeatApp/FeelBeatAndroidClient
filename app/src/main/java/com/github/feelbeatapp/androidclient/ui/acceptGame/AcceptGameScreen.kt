package com.github.feelbeatapp.androidclient.ui.acceptGame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.github.feelbeatapp.androidclient.ui.startGame.Player
import com.github.feelbeatapp.androidclient.ui.startGame.PlayerCard

@Composable
fun AcceptScreen(
    parentNavController: NavHostController,
    viewModel: AcceptViewModel = AcceptViewModel(),
) {
    val players = viewModel.players.collectAsState().value
    val playlist = viewModel.playlist.collectAsState().value
    val snippetDuration = viewModel.snippetDuration.collectAsState().value
    val pointsToWin = viewModel.pointsToWin.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(onClick = { /* TODO home */ }) {
            Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Back")
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            players.forEach { player ->
                PlayerCard(player = player)
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text("Snippet Duration: $snippetDuration seconds", style = MaterialTheme.typography.bodyMedium)
            Text("Points to Win: $pointsToWin", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Playlist: ${playlist.name}", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            playlist.songs.forEach { song ->
                SongItem(song = song)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* TODO startgame */ },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape
        ) {
            Text("PLAY", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun PlayerCard(player: Player) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = player.image),
            contentDescription = "Player Image",
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Text(
            text = player.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
fun SongItem(song: Song) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = song.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun PreviewAcceptScreen() {
    val navController = rememberNavController()
    AcceptScreen(parentNavController = navController)
}
