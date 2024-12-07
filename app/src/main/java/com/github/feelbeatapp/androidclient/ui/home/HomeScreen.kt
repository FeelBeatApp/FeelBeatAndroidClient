package com.github.feelbeatapp.androidclient.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import androidx.compose.foundation.lazy.items


@Composable
fun HomeScreen(parentNavController: NavHostController,
               viewModel: HomeViewModel = HomeViewModel(),
               modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val title = currentBackStack?.destination?.route ?: "FeelBeat"

    val rooms by viewModel.rooms.collectAsState()
    val selectedRoom by viewModel.selectedRoom.collectAsState()

    Scaffold(topBar = { HomeTopBar(title) }) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding).fillMaxSize()) {
            Text(
                text = "Aktualne rozgrywki",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rooms) { room ->
                    RoomItem(
                        room = room,
                        isSelected = room == selectedRoom,
                        onClick = { viewModel.selectRoom(room) }
                    )
                }
            }

//            NavHost(navController, startDestination = HomeRoute.HOME.name) {
//                composable(route = HomeRoute.HOME.name) { Text("Here list of games") }
//
//                composable(route = HomeRoute.CHOOSE_PLAYLIST.name) { Text("Here choose playlist") }
//
//                composable(route = HomeRoute.GAME_SETTINGS.name) {
//                    Text("Here choose game settings")
//                }
//            }

//            Row {
//                Button(onClick = { navController.navigate(HomeRoute.HOME.name) }) { Text("Home") }
//
//                Button(onClick = { navController.navigate(HomeRoute.CHOOSE_PLAYLIST.name) }) {
//                    Text("Playlists")
//                }
//
//                Button(onClick = { navController.navigate(HomeRoute.GAME_SETTINGS.name) }) {
//                    Text("Settings")
//                }
//            }
//
//            Button(onClick = { parentNavController.navigate(FeelBeatRoute.GAME.name) }) {
//                Text("Game")
//            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
            ) {
                Button(onClick = { /*TODO new room settings*/},
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-15).dp, y = (-120).dp)
                        .size(60.dp),
                ) {
                    //KUBA JA NIE WIEM CZEMU PLUSIK NIE JEST NA ŚRODKU, JAK SIĘ DA MINUSA TO JEST, RATUNKU :((((
                    Text("+", style = MaterialTheme.typography.headlineMedium)
                }
                Button(
                    onClick = { /* TODO accept screen */ },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.8f)
                        .height(60.dp),
                    enabled = selectedRoom != null
                ) {
                    Text("NEXT", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(title: String) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        actions = {
            IconButton(onClick = {/*TODO account settings*/}) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = stringResource(R.string.account),
                )
            }
        },
    )
}

@Composable
fun RoomItem(
    room: Room,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* TODO Edit room */ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Room")
            }
        }
    }
}


@Preview
@Composable
fun HomePreview() {
    val nav = rememberNavController()
    HomeScreen(nav)
}
