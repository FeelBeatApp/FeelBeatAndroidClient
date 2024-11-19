package com.github.feelbeatapp.androidclient.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme
import kotlin.math.exp

@Composable
fun HomeScreen(parentNavController: NavHostController, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val title = currentBackStack?.destination?.route ?: "FeelBeat"

    Scaffold(
        topBar = { HomeTopBar(title) },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(navController, startDestination = HomeRoute.HOME.name) {
                composable(route = HomeRoute.HOME.name) {
                    Text("Here list of games")
                }

                composable(route = HomeRoute.CHOOSE_PLAYLIST.name) {
                    Text("Here choose playlist")
                }

                composable(route = HomeRoute.GAME_SETTINGS.name) {
                    Text("Here choose game settings")
                }
            }

            Row {
                Button(onClick = { navController.navigate(HomeRoute.HOME.name) }) {
                    Text("Home")
                }

                Button(onClick = { navController.navigate(HomeRoute.CHOOSE_PLAYLIST.name) }) {
                    Text("Playlists")
                }

                Button(onClick = { navController.navigate(HomeRoute.GAME_SETTINGS.name) }) {
                    Text("Settings")
                }
            }

            HorizontalDivider()

            Button(onClick = { parentNavController.navigate(FeelBeatRoute.GAME.name) }) {
                Text("Game")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(title: String) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.notifications)
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = stringResource(R.string.account)
                )
            }
        }
    )
}

@Preview
@Composable
fun HomePreview() {
    FeelBeatTheme {
        val nav = rememberNavController()
        HomeScreen(nav)
    }
}