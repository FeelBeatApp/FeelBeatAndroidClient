package com.github.feelbeatapp.androidclient.ui.app.game.guesssong

import androidx.annotation.OptIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.game.model.GuessCorrectness
import com.github.feelbeatapp.androidclient.game.model.Song
import com.github.feelbeatapp.androidclient.ui.app.components.SongCard
import com.github.feelbeatapp.androidclient.ui.app.game.components.PlayerGameBadge
import com.github.feelbeatapp.androidclient.ui.app.game.guesssong.components.AudioPlayerControls

@OptIn(UnstableApi::class)
@Composable
fun GuessSongScreen(viewModel: GuessSongViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

    LaunchedEffect(null) { viewModel.play() }

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {
                items(uiState.players) { p ->
                    PlayerGameBadge(imageUrl = p.player.imageUrl, size = 40.dp, result = p.status)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                VerticalDivider(modifier = Modifier.height(32.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    uiState.cumulatedPoints.toString(),
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("+", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    uiState.pointsToWin.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("points")
            }
        }

        HorizontalDivider()

        SearchBar(
            searchQuery = uiState.query,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().weight(1f),
        ) {
            items(uiState.songs.filter { it !in uiState.blacklisted }) { s ->
                SongItem(
                    song = s.song,
                    onClick = { viewModel.guess(s.song.id) },
                    correctness = s.status,
                    penalty = uiState.pointsPenalty,
                    removeFromList = { viewModel.blackList(s) },
                )
            }
        }

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

@Composable
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextField(
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
            },
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun SongItem(
    song: Song,
    onClick: () -> Unit,
    correctness: GuessCorrectness,
    penalty: Int,
    removeFromList: () -> Unit,
) {
    val fade by
        animateFloatAsState(
            if (correctness == GuessCorrectness.INCORRECT) 1f else 0f,
            animationSpec = tween(durationMillis = 300),
        )

    LaunchedEffect(fade) {
        if (fade == 1f) {
            removeFromList()
        }
    }

    Box(modifier = Modifier.height(50.dp)) {
        SongCard(
            title = song.title,
            artist = song.artist,
            imageUrl = song.imageUrl,
            duration = song.duration,
            onClick = onClick,
            size = 50.dp,
            displayDuration = false,
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier.alpha(fade)
                    .fillMaxSize()
                    .clipToBounds()
                    .clip(CardDefaults.elevatedShape)
                    .background(MaterialTheme.colorScheme.errorContainer),
        ) {
            Text(
                "- $penalty points",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
fun GuessSongPreview() {
    GuessSongScreen()
}
