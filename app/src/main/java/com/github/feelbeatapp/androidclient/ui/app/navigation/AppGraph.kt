package com.github.feelbeatapp.androidclient.ui.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.game.model.RoomStage
import com.github.feelbeatapp.androidclient.ui.app.AppScreen
import com.github.feelbeatapp.androidclient.ui.app.home.HomeScreen
import com.github.feelbeatapp.androidclient.ui.app.lobby.components.LobbyBottomBar
import com.github.feelbeatapp.androidclient.ui.app.lobby.viewmodels.LobbyViewModel
import com.github.feelbeatapp.androidclient.ui.app.roomsettings.screens.NewRoomSettingsScreen
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun AppGraph(
    onLogout: () -> Unit,
    lobbyViewModel: LobbyViewModel = hiltViewModel(),
    navigationViewModel: NavigationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route
    val lobbyState by lobbyViewModel.lobbyState.collectAsStateWithLifecycle()
    val stage by navigationViewModel.stage.collectAsStateWithLifecycle(RoomStage.LOBBY)
    val loadedRoomId by navigationViewModel.roomId.collectAsStateWithLifecycle(null)

    LaunchedEffect(lobbyState.joinFailed) {
        if (lobbyState.joinFailed) {
            navController.popBackStack(AppRoute.HOME.route, inclusive = false)
            lobbyViewModel.reset()
        }
    }

    LaunchedEffect(stage) {
        if (stage == RoomStage.GAME) {
            navController.navigate(
                AppRoute.START_GAME.withArgs(mapOf("roomId" to (loadedRoomId ?: "")))
            ) {
                popUpTo(AppRoute.HOME.route)
            }
        }
    }

    var showExitDialog by remember { mutableStateOf(false) }

    ExitDialog(
        showExitDialog = showExitDialog,
        onChange = { showExitDialog = it },
        onNavigateBack = { navController.popBackStack(AppRoute.HOME.route, inclusive = false) },
    )

    if (route != AppRoute.HOME.route) {
        BackHandler { getNavigateBackBehaviour(route, navController, { showExitDialog = true })() }
    }

    AppScreen(
        title = getRouteTitle(route),
        backVisible = getBackButtonVisibility(route),
        onLogout = onLogout,
        onNavigateBack = getNavigateBackBehaviour(route, navController, { showExitDialog = true }),
        bottomBar =
            getBottomBar(
                route,
                onNavigate = {
                    val roomId = navBackStackEntry?.getRoomId()
                    if (roomId != null) {
                        navController.navigate(it.withArgs(mapOf("roomId" to roomId)))
                    }
                },
            ),
    ) {
        NavHost(navController = navController, startDestination = AppRoute.HOME.route) {
            composable(route = AppRoute.HOME.route) {
                HomeScreen(
                    onRoomSelect = { roomId ->
                        lobbyViewModel.joinRoom(roomId)
                        navController.navigate(
                            AppRoute.ROOM_LOBBY.withArgs(mapOf("roomId" to roomId))
                        )
                    },
                    onNewRoom = { navController.navigate(AppRoute.NEW_ROOM.route) },
                )
            }
            composable(route = AppRoute.NEW_ROOM.route) {
                NewRoomSettingsScreen(
                    onRoomCreated = {
                        lobbyViewModel.joinRoom(it)
                        navController.navigate(
                            AppRoute.ROOM_LOBBY.withArgs(mapOf("roomId" to it))
                        ) {
                            popUpTo(AppRoute.HOME.route)
                        }
                    }
                )
            }
            navigation(route = AppRoute.ROOM.route, startDestination = AppRoute.ROOM_LOBBY.route) {
                lobbyGraph()
            }

            navigation(route = AppRoute.GAME.route, startDestination = AppRoute.START_GAME.route) {
                gameGraph(
                    onNavigate = { navController.navigate(it) { popUpTo(AppRoute.HOME.route) } }
                )
            }
        }
    }
}

@Composable
fun ExitDialog(showExitDialog: Boolean, onChange: (Boolean) -> Unit, onNavigateBack: () -> Unit) {
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { onChange(false) },
            title = { Text(text = stringResource(R.string.exit_room)) },
            text = {
                Text(
                    text = stringResource(R.string.are_you_sure),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right,
                ) {
                    TextButton(
                        onClick = {
                            onChange(false)
                            onNavigateBack()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.yes),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    TextButton(
                        onClick = { onChange(false) },
                        modifier =
                            Modifier.background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = MaterialTheme.shapes.small,
                                )
                                .padding(horizontal = 4.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.no),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            },
        )
    }
}

@Composable
fun getRouteTitle(route: String?): String {
    return stringResource(
        when (route) {
            AppRoute.ROOM_LOBBY.route -> R.string.get_ready
            AppRoute.ROOM_SONGS.route -> R.string.playlist_content
            AppRoute.ROOM_EDIT.route -> R.string.edit_room
            AppRoute.NEW_ROOM.route -> R.string.create_room
            AppRoute.GUESS.route -> R.string.guess_the_song
            else -> R.string.app_name
        }
    )
}

fun getBackButtonVisibility(route: String?): Boolean {
    return when (route) {
        AppRoute.HOME.route -> false
        else -> true
    }
}

fun getBottomBar(route: String?, onNavigate: (AppRoute) -> Unit): @Composable () -> Unit {
    return when (route) {
        AppRoute.ROOM_LOBBY.route -> ({
                LobbyBottomBar(AppRoute.ROOM_LOBBY, onNavigate = onNavigate)
            })
        AppRoute.ROOM_SONGS.route -> ({
                LobbyBottomBar(AppRoute.ROOM_SONGS, onNavigate = onNavigate)
            })
        AppRoute.ROOM_EDIT.route -> ({
                LobbyBottomBar(AppRoute.ROOM_EDIT, onNavigate = onNavigate)
            })
        else -> ({})
    }
}

fun getNavigateBackBehaviour(
    route: String?,
    navController: NavHostController,
    showDialog: () -> Unit,
): () -> Unit {
    return when (route) {
        AppRoute.HOME.route -> ({})
        AppRoute.NEW_ROOM.route -> ({ navController.popBackStack(AppRoute.HOME.route, false) })
        else -> showDialog
    }
}

fun NavBackStackEntry.getRoomId(): String? {
    val roomId = arguments?.getString("roomId")
    return roomId
}

@Composable
@Preview
fun AppInternalRouterPreview() {
    FeelBeatTheme { AppGraph({}) }
}
