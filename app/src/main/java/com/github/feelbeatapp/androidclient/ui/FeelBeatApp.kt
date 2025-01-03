package com.github.feelbeatapp.androidclient.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.ui.gameresult.GameResultScreen
import com.github.feelbeatapp.androidclient.ui.guesssong.GuessResultScreen
import com.github.feelbeatapp.androidclient.ui.guesssong.GuessSongScreen
import com.github.feelbeatapp.androidclient.ui.home.HomeScreen
import com.github.feelbeatapp.androidclient.ui.login.LoginScreen
import com.github.feelbeatapp.androidclient.ui.roomsettings.screens.NewRoomSettingsScreen
import com.github.feelbeatapp.androidclient.ui.startgame.StartGameScreen
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun FeelBeatApp(startScreen: FeelBeatRoute, modifier: Modifier = Modifier) {
    FeelBeatTheme {
        val navController = rememberNavController()

        Box(modifier = modifier) {
            NavHost(navController, startDestination = startScreen.name) {
                composable(route = FeelBeatRoute.LOGIN.name) { LoginScreen() }
                composable(route = FeelBeatRoute.HOME.name) {
                    HomeScreen(navController = navController)
                }
                composable(route = FeelBeatRoute.NEW_ROOM_SETTINGS.name) {
                    NewRoomSettingsScreen(
                        onNavigateTo = { navController.navigate(it) },
                        onNavigateBack = { navController.popBackStack() },
                    )
                }
                composable(route = FeelBeatRoute.GAME_RESULT.name) {
                    GameResultScreen(navController = navController)
                }
                composable(route = FeelBeatRoute.GUESS_SONG.name) {
                    GuessSongScreen(navController = navController)
                }
                composable(route = FeelBeatRoute.GUESS_RESULT.name) {
                    GuessResultScreen(navController = navController)
                }
                composable(route = FeelBeatRoute.START_GAME.name) {
                    StartGameScreen(navController = navController)
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    FeelBeatApp(FeelBeatRoute.LOGIN)

}
