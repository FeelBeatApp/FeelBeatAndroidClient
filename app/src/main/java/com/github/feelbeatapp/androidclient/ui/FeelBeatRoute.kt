package com.github.feelbeatapp.androidclient.ui

enum class FeelBeatRoute {
    LOGIN,
    HOME,
    NEW_ROOM_SETTINGS,
    ROOM_SETTINGS,
    ACCEPT_GAME,
    GAME_RESULT,
    GUESS_SONG,
    GUESS_RESULT,
    START_GAME;

    fun withArgs(vararg args: String): String {
        return buildString {
            append(name)
            args.forEach { append("/$it") }
        }
    }
}
