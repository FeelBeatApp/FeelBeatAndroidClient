package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.game.model.GuessCorrectness
import com.github.feelbeatapp.androidclient.ui.app.game.components.PlayerGameBadge
import com.github.feelbeatapp.androidclient.ui.app.game.guesssong.components.AudioPlayerControls

const val CARD_WIDTH = .8f

@Composable
fun GuessResultScreen(viewModel: GuessSongViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

    LaunchedEffect(null) {
        viewModel.pause()
        viewModel.play()
    }

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
                uiState.players.forEach { playerWithResult ->
                    PlayerGameBadge(
                        imageUrl = playerWithResult.player.imageUrl,
                        points = uiState.pointsMap[playerWithResult.player.id] ?: 0,
                        size = 40.dp,
                        result = playerWithResult.status,
                    )
                }
            }
        }

        val correctSong = uiState.songs.find { it.status == GuessCorrectness.CORRECT }?.song
        if (correctSong != null) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().heightIn(0.dp, 500.dp).weight(1f).padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(CARD_WIDTH).heightIn(0.dp, 300.dp),
                    ) {
                        AsyncImage(
                            model = correctSong.imageUrl,
                            contentDescription = "song",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    Text(
                        text = correctSong.title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                    )

                    Text(
                        text = correctSong.artist,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }
        }

        Text(
            text =
                if (uiState.lastGuessCorrect) {
                    stringResource(R.string.you_guessed_song_correctly)
                } else {
                    stringResource(R.string.ups_that_s_not_correct_answer)
                },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp),
        )

        HorizontalDivider()

        AudioPlayerControls(
            value = playbackState.progress,
            onValueChange = { viewModel.seek(it) },
            duration = uiState.songDuration,
            isPlaying = playbackState.isPlaying,
            onPlayPause = { isPlaying -> if (isPlaying) viewModel.pause() else viewModel.play() },
        )
    }
}

@Preview
@Composable
fun GuessResultScreenPreview() {
    GuessResultScreen()
}
