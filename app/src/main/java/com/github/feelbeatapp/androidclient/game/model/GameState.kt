package com.github.feelbeatapp.androidclient.game.model

data class GameState(
    val roomId: String,
    val playlistName: String,
    val playlistImageUrl: String,
    val adminId: String,
    val me: String,
    val players: List<Player>,
    val songs: List<Song>,
    val settings: RoomSettings,
    val readyMap: Map<String, Boolean>,
    val stage: RoomStage,
    val audio: AudioState?,
    val pointsMap: Map<String, Int>,
    val songGuessMap: Map<String, GuessCorrectness>,
    val playerGuessMap: Map<String, GuessCorrectness>,
)
