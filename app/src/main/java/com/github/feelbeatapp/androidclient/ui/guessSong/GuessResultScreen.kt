package com.github.feelbeatapp.androidclient.ui.guessSong

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun GuessResultScreen(
    navController: NavController,
    viewModel: GuessSongViewModel = GuessSongViewModel()
) {
  val players by viewModel.players.collectAsState()
  val currentSong by viewModel.currentSong.collectAsState()
  val result by viewModel.result.collectAsState()

  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {
              Row(
                  horizontalArrangement = Arrangement.SpaceEvenly,
                  modifier = Modifier.fillMaxWidth()) {
                    players.forEach { player ->
                      PlayerStatusIcon(
                          image = player.image, isCorrect = (player.status == ResultStatus.CORRECT))
                    }
                  }
              currentSong?.let { song ->
                Box(
                    modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center) {
                      SongInfo(songName = song.name, artistName = song.artist)
                    }
              }
            }

        result?.let {
          Text(
              text = if (it.isCorrect) "You guessed the song correctly!" else "You guessed wrong!",
              style = MaterialTheme.typography.headlineLarge,
              fontWeight = FontWeight.Bold,
              color =
                  if (it.isCorrect) MaterialTheme.colorScheme.primary
                  else MaterialTheme.colorScheme.error,
              modifier = Modifier.padding(top = 16.dp))
        }

        result?.let {
          Text(
              text = "Points: ${it.points}",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(bottom = 32.dp))
        }
      }
}

@Composable
fun PlayerStatusIcon(image: Int, isCorrect: Boolean) {
  Box(contentAlignment = Alignment.TopEnd) {
    Image(
        painter = painterResource(id = image),
        contentDescription = "Player Avatar",
        modifier = Modifier.size(60.dp).clip(CircleShape))
    Text(
        text = if (isCorrect) "✔" else "✖",
        style = MaterialTheme.typography.bodySmall,
        color =
            if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp))
  }
}

@Composable
fun SongInfo(songName: String, artistName: String) {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        //        Image(
        //            painter = painterResource(id = R.drawable.ic_playlist_image), // Replace with
        // song image resource
        //            contentDescription = "Song Image",
        //            modifier = Modifier
        //                .size(50.dp)
        //                .clip(CircleShape)
        //        )
        Column {
          Text(
              text = songName,
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.Bold)
          Text(text = "By $artistName", style = MaterialTheme.typography.bodySmall)
        }
      }
}

@Preview
@Composable
fun GuessResultScreenPreview() {
  val nav = rememberNavController()
  GuessResultScreen(navController = nav)
}
