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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute

@Composable
fun GuessResultScreen(
    navController: NavController,
    viewModel: GuessSongViewModel = GuessSongViewModel(),
) {
    val guessState by viewModel.guessState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                guessState.players.forEach { playerWithResult ->
                    PlayerStatusIcon(
                        image = playerWithResult.player.image,
                        isCorrect = (playerWithResult.resultStatus == ResultStatus.CORRECT),
                    )
                }
            }

            guessState.currentSong?.let { song ->
                Box(
                    modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    SongInfo(songTitle = song.title)
                }
            }
        }

        Text(
            text =
                if (guessState.players.any { it.resultStatus == ResultStatus.CORRECT }) {
                    stringResource(R.string.you_guessed_song_correctly)
                } else {
                    stringResource(R.string.ups_that_s_not_correct)
                },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp),
        )

        Text(
            text = "Points: ${guessState.players.sumOf { it.points }}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        Button(onClick = { navController.navigate(FeelBeatRoute.GUESS_SONG.name) }) {
            Text(text = "NEXT")
        }
    }
}

@Composable
fun PlayerStatusIcon(image: Int, isCorrect: Boolean) {
    Box(contentAlignment = Alignment.TopEnd) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Player Avatar",
            modifier = Modifier.size(60.dp).clip(CircleShape),
        )
        Text(
            text = if (isCorrect) "✔" else "✖",
            style = MaterialTheme.typography.bodySmall,
            color =
                if (isCorrect) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error,
            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
        )
    }
}

@Composable
fun SongInfo(songTitle: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column {
            Text(
                text = songTitle,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
fun GuessResultScreenPreview() {
    val nav = rememberNavController()
    GuessResultScreen(navController = nav)
}
