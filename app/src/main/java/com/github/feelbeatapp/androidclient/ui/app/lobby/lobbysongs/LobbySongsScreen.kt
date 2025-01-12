package com.github.feelbeatapp.androidclient.ui.app.lobby.lobbysongs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.feelbeatapp.androidclient.ui.app.components.SongCard
import com.github.feelbeatapp.androidclient.ui.loading.LoadingScreen
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun LobbySongsScreen(viewModel: LobbySongsViewModel = hiltViewModel()) {
    val songs by viewModel.songs.collectAsStateWithLifecycle()

    if (songs.isEmpty()) {
        LoadingScreen()
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        items(songs) { song ->
            SongCard(
                title = song.title,
                artist = song.artist,
                imageUrl = song.imageUrl,
                duration = song.duration,
                size = 80.dp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LobbySongsScreenPreview() {
    FeelBeatTheme { LobbySongsScreen() }
}
