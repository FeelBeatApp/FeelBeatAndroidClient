package com.github.feelbeatapp.androidclient.ui.acceptGame

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.github.feelbeatapp.androidclient.ui.startGame.Player
import com.github.feelbeatapp.androidclient.ui.startGame.PlayerCard

@Composable
fun AcceptGameScreen(viewModel: AcceptViewModel = AcceptViewModel(), navController: NavController) {
  val players = viewModel.players.collectAsState().value
  val playlist = viewModel.playlist.collectAsState().value
  val snippetDuration = viewModel.snippetDuration.collectAsState().value
  val pointsToWin = viewModel.pointsToWin.collectAsState().value

  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    IconButton(onClick = { navController.navigate(FeelBeatRoute.ACCEPT_GAME.name) }) {
      Icon(
          Icons.AutoMirrored.Filled.KeyboardArrowLeft,
          contentDescription = stringResource(R.string.back))
    }

    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
      players.forEach { player -> PlayerCard(player = player) }
    }

    Spacer(modifier = Modifier.height(32.dp))

    Column(horizontalAlignment = Alignment.Start) {
      Text(
          text = stringResource(id = R.string.snippet_duration_val, snippetDuration),
          style = MaterialTheme.typography.bodyMedium)
      Text(
          text = stringResource(id = R.string.points_to_win_val, pointsToWin),
          style = MaterialTheme.typography.bodyMedium)
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = stringResource(id = R.string.playlist_name, playlist.name),
        style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(8.dp))

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxWidth()) {
      playlist.songs.forEach { song -> SongItem(song = song) }
    }

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = { navController.navigate(FeelBeatRoute.START_GAME.name) },
        modifier = Modifier.fillMaxWidth(),
        shape = CircleShape) {
          Text(stringResource(R.string.play), style = MaterialTheme.typography.headlineMedium)
        }
  }
}

@Composable
fun PlayerCard(player: Player) {
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
    Image(
        painter = painterResource(id = player.image),
        contentDescription = stringResource(R.string.player_image),
        modifier =
            Modifier.size(20.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape))
    Text(
        text = player.name,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 10.dp))
  }
}

@Composable
fun SongItem(song: Song) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f))
      }
}

@Preview
@Composable
fun PreviewAcceptScreen() {
  val navController = rememberNavController()
  AcceptGameScreen(navController = navController)
}
