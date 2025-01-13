package com.github.feelbeatapp.androidclient.ui.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.feelbeatapp.androidclient.ui.app.game.gameresult.GameResultScreen
import com.github.feelbeatapp.androidclient.ui.app.game.guesssong.GuessResultScreen
import com.github.feelbeatapp.androidclient.ui.app.game.guesssong.GuessSongScreen
import com.github.feelbeatapp.androidclient.ui.app.game.startgame.StartGameScreen

fun NavGraphBuilder.gameGraph(onNavigate: (String) -> Unit) {
    composable(route = AppRoute.START_GAME.route) {
        StartGameScreen(roomId = it.getRoomId()!!, onNavigate = onNavigate)
    }

    composable(route = AppRoute.GUESS.route) { GuessSongScreen() }

    composable(route = AppRoute.GUESS_RESULT.route) {
        GuessResultScreen(roomId = it.getRoomId()!!, onNavigate = onNavigate)
    }

    composable(route = AppRoute.GAME_RESULT.route) { GameResultScreen(onNavigate = onNavigate) }
}
