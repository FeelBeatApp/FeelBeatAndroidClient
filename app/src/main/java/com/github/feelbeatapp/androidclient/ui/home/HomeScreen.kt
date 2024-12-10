package com.github.feelbeatapp.androidclient.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.FeelBeatRoute
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun HomeScreen(
    parentNavController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val profile by homeViewModel.profile.collectAsState()
    val title = currentBackStack?.destination?.route ?: "FeelBeat"

    LaunchedEffect(Unit) { homeViewModel.triggerProfileLoading() }

    Scaffold(topBar = { HomeTopBar(title) }) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding).fillMaxSize()) {
            NavHost(navController, startDestination = HomeRoute.HOME.name) {
                composable(route = HomeRoute.HOME.name) { Text("Here list of games") }

                composable(route = HomeRoute.CHOOSE_PLAYLIST.name) { Text("Here choose playlist") }

                composable(route = HomeRoute.GAME_SETTINGS.name) {
                    Text("Here choose game settings")
                }
            }

            Row {
                Button(onClick = { navController.navigate(HomeRoute.HOME.name) }) { Text("Home") }

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

            HorizontalDivider()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp),
            ) {
                if (profile == null) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 8.dp,
                        modifier = Modifier.width(50.dp).height(50.dp),
                    )
                } else {
                    Text(profile?.displayName ?: "")
                    Text(profile?.email ?: "")

                    AsyncImage(
                        model = profile?.images?.get(0)?.url,
                        contentDescription = "profile pic",
                        modifier =
                            Modifier.padding(0.dp, 20.dp)
                                .clip(CircleShape)
                                .width(300.dp)
                                .height(300.dp),
                    )
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
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.notifications),
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = stringResource(R.string.account),
                )
            }
        },
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
