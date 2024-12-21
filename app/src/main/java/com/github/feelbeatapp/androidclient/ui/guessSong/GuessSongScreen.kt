package com.github.feelbeatapp.androidclient.ui.guessSong

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessSongScreen(
    navController: NavController,
    viewModel: GuessSongViewModel = GuessSongViewModel()
) {
  val players by viewModel.players.collectAsState()
  val playlist by viewModel.playlist.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()

  Scaffold(topBar = { TopAppBar(title = { Text("Playlist#1") }) }) { paddingValues ->
    Column(
        modifier = Modifier.padding(paddingValues).fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
          Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()) {
                players.forEach { player -> PlayerStatusIcon(player = player) }
              }

          MusicControlSlider()

          Column {
            Text(
                text = stringResource(R.string.guess_the_song),
                style = MaterialTheme.typography.bodyMedium)
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) })
          }

          LazyColumn(
              verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
                items(playlist.size) { index ->
                  SongItem(
                      song = playlist[index],
                      onClick = { navController.navigate(FeelBeatRoute.GUESS_RESULT.name) })
                }
              }
        }
  }
}

@Composable
fun PlayerStatusIcon(player: Player1) {
  val icon =
      when (player.status) {
        // PlayerStatus.CORRECT -> Icons.Outlined.CheckCircle
        // PlayerStatus.WRONG -> Icons.Outlined.Error
        ResultStatus.NORESPONSE -> null
        else -> null
      }
  val color =
      when (player.status) {
        //        PlayerStatus.CORRECT -> Color.Green
        //        PlayerStatus.WRONG -> Color.Red
        //        PlayerStatus.UNANSWERED -> Color.Gray
        else -> null
      }

  Box(modifier = Modifier.size(48.dp)) {
    Image(
        painter = painterResource(id = player.image),
        contentDescription = stringResource(R.string.player_avatar),
        modifier = Modifier.size(48.dp).clip(CircleShape))
    icon?.let {
      Icon(
          imageVector = it,
          contentDescription = null,
          modifier = Modifier.align(Alignment.BottomEnd).size(16.dp))
    }
  }
}

@Composable
fun MusicControlSlider() {
  Column {
    Text(stringResource(R.string.music_control), style = MaterialTheme.typography.bodyMedium)
    Slider(
        value = 0.5f,
        onValueChange = { /* TODO Handle slider change */ },
        modifier = Modifier.fillMaxWidth())
  }
}

@Composable
fun SearchBar(searchQuery: TextFieldValue, onSearchQueryChange: (TextFieldValue) -> Unit) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
              .padding(8.dp),
      verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            singleLine = true,
            modifier = Modifier.weight(1f))
        Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
      }
}

@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
  Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
          Column { Text(song.name, style = MaterialTheme.typography.bodyLarge) }
          //            Image(
          //                painter = painterResource(id = song.image),
          //                contentDescription = null,
          //                modifier = Modifier.size(48.dp)
          //            )
        }
  }
}

@Preview
@Composable
fun GuessSongPreview() {
  val nav = rememberNavController()
  GuessSongScreen(navController = nav)
}