package com.github.feelbeatapp.androidclient.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.ui.game.GameScreen
import com.github.feelbeatapp.androidclient.ui.home.HomeScreen
import com.github.feelbeatapp.androidclient.ui.login.LoginScreen
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun FeelBeatApp(
    @Suppress("UnusedParameter") widthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    authViewModel: FeelBeatAppViewModel = hiltViewModel<FeelBeatAppViewModel>(),
) {
    val appState by authViewModel.appState.collectAsStateWithLifecycle()
    val startRoute =
        if (appState.isAuthorized) FeelBeatRoute.HOME.name else FeelBeatRoute.LOGIN.name

    FeelBeatTheme {
        val navController = rememberNavController()

        Box(modifier = modifier) {
            NavHost(navController, startDestination = startRoute) {
                composable(route = FeelBeatRoute.LOGIN.name) {
                    LoginScreen(onLoggedIn = { navController.navigate(FeelBeatRoute.HOME.name) })
                }

                composable(route = FeelBeatRoute.HOME.name) {
                    HomeScreen(parentNavController = navController)
                }

                composable(route = FeelBeatRoute.GAME.name) { GameScreen() }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    FeelBeatApp(WindowWidthSizeClass.Compact)
}
