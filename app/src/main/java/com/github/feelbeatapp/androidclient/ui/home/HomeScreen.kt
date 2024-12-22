package com.github.feelbeatapp.androidclient.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = HomeViewModel(),
    modifier: Modifier = Modifier,
    navController: NavController,
) {
  val title = stringResource(R.string.feel_beat)
  val rooms by viewModel.rooms.collectAsState()
  val selectedRoom by viewModel.selectedRoom.collectAsState()

  Scaffold(topBar = { HomeTopBar(title, navController) }) { innerPadding ->
    Column(modifier = modifier.padding(innerPadding).fillMaxSize()) {
      Text(
          text = stringResource(R.string.current_games),
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(16.dp))
      LazyColumn(
          modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(rooms) { room ->
              RoomItem(
                  room = room,
                  isSelected = room == selectedRoom,
                  onClick = { navController.navigate(FeelBeatRoute.ACCEPT_GAME.name) })
            }
          }

      Box(modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp, start = 16.dp, end = 16.dp)) {
        Box(
            modifier =
                Modifier.align(Alignment.BottomEnd)
                    .offset(x = (-15).dp)
                    .size(60.dp)
                    .background(
                        MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)) {
              IconButton(
                  onClick = { navController.navigate(FeelBeatRoute.NEW_ROOM_SETTINGS.name) },
                  modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary)
                  }
            }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(title: String, navController: NavController) {
  CenterAlignedTopAppBar(
      title = { Text(title) },
      colors =
          TopAppBarDefaults.topAppBarColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer,
              titleContentColor = MaterialTheme.colorScheme.primary,
          ),
      actions = {
        IconButton(onClick = { navController.navigate(FeelBeatRoute.ACCOUNT_SETTINGS.name) }) {
          Icon(
              imageVector = Icons.Outlined.Person,
              contentDescription = stringResource(R.string.account),
          )
        }
      },
  )
}

@Composable
fun RoomItem(room: Room, isSelected: Boolean, onClick: () -> Unit) {
  Card(
      onClick = onClick,
      colors =
          CardDefaults.cardColors(
              containerColor =
                  if (isSelected) MaterialTheme.colorScheme.primaryContainer
                  else MaterialTheme.colorScheme.surface),
      modifier =
          Modifier.fillMaxWidth()
              .padding(8.dp)
              .border(
                  width = 1.dp,
                  color = MaterialTheme.colorScheme.outline,
                  shape = MaterialTheme.shapes.medium)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
          Text(
              text = room.name,
              style = MaterialTheme.typography.bodyLarge,
              modifier = Modifier.weight(1f))
        }
      }
}

@Preview
@Composable
fun HomePreview() {
  val navController = rememberNavController()
  HomeScreen(navController = navController)
}
