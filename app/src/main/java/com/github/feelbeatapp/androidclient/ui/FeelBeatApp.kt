package com.github.feelbeatapp.androidclient.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.feelbeatapp.androidclient.ui.app.navigation.AppGraph
import com.github.feelbeatapp.androidclient.ui.login.LoginScreen
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun FeelBeatApp(startScreen: RootRoute, modifier: Modifier = Modifier) {
    FeelBeatTheme {
        val navController = rememberNavController()

        Box(modifier = modifier) {
            NavHost(navController, startDestination = startScreen.name) {
                composable(route = RootRoute.APP.name) {
                    AppGraph(
                        onLogout = {
                            navController.navigate(RootRoute.LOGIN.name) {
                                popUpTo(navController.graph.id)
                            }
                        }
                    )
                }
                composable(route = RootRoute.LOGIN.name) { LoginScreen() }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    FeelBeatApp(RootRoute.LOGIN)
}
