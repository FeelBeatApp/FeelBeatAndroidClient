package com.github.feelbeatapp.androidclient.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.github.feelbeatapp.androidclient.ui.game.GameScreen
import com.github.feelbeatapp.androidclient.ui.home.HomeScreen
import com.github.feelbeatapp.androidclient.ui.login.LoginScreen
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun FeelBeatApp(
    @Suppress("UnusedParameter") widthSizeClass: WindowWidthSizeClass,
    startRoute: FeelBeatRoute,
    modifier: Modifier = Modifier,
) {
    FeelBeatTheme {
        val navController = rememberNavController()

        Box(modifier = modifier) {
            NavHost(navController, startDestination = startRoute.name) {
                composable(route = FeelBeatRoute.LOGIN.name) { LoginScreen() }

                composable(route = FeelBeatRoute.HOME.name) {
                    HomeScreen(parentNavController = navController)
                }

                //composable(route = FeelBeatRoute.GAME.name) { GameScreen() }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    FeelBeatApp(WindowWidthSizeClass.Compact, FeelBeatRoute.HOME)
}
