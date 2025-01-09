package com.github.feelbeatapp.androidclient.media

import android.annotation.SuppressLint
import android.content.Intent
import androidx.media3.common.MediaItem
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService


class MusicService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer

    private val binder = MusicBinder()

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    fun getPlayer(): ExoPlayer = player

    fun playSongFromUrl(songUrl: String) {
        val mediaItem = MediaItem.fromUri(songUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    @SuppressLint("MissingSuperCall")
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}

