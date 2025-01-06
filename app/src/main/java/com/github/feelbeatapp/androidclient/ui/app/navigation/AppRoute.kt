package com.github.feelbeatapp.androidclient.ui.app.navigation

enum class AppRoute(val route: String) {
    HOME("home"),
    NEW_ROOM("new_room"),
    ROOM("room"),
    ROOM_LOBBY("room/{roomId}"),
    ROOM_EDIT("room/{roomId}/edit"),
    GAME("game"),
    START_GAME("game/{roomId}"),
    GUESS("game/{roomId}/guess"),
    GUESS_RESULT("game/{roomId}/guess_result"),
    GAME_RESULT("game/{roomId}/result");

    fun withArgs(args: Map<String, String>): String =
        args.entries.fold(route) { acc, (key, value) -> acc.replace("{$key}", value) }
}
