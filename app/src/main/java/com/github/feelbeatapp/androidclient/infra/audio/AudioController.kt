package com.github.feelbeatapp.androidclient.infra.audio

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

const val FEELBEAT_LOGO_URL =
    "https://github.com/FeelBeatApp/FeelBeatAndroidClient/blob/main/app/src/main/res/drawable/logo.svg"

data class PlaybackState(val isPlaying: Boolean = false, val progress: Long = 0)

class AudioController @Inject constructor(@ApplicationContext ctx: Context) : Listener {
    private var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController
    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState = _playbackState.asStateFlow()

    init {
        val sessionToken = SessionToken(ctx, ComponentName(ctx, AudioPlaybackService::class.java))
        controllerFuture = MediaController.Builder(ctx, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                controller = controllerFuture.get()
                controller.addListener(this)
            },
            MoreExecutors.directExecutor(),
        )
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int,
    ) {
        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
        _playbackState.update { it.copy(progress = newPosition.positionMs) }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        _playbackState.update { it.copy(isPlaying = isPlaying) }
    }

    fun loadAudioFromUri(uri: String, duration: Long) {
        if (!::controller.isInitialized) {
            controllerFuture.addListener(
                { loadAudioFromUri(uri, duration) },
                MoreExecutors.directExecutor(),
            )
        } else {
            controller.setMediaItem(getMediaItemFromUri(uri))
        }
    }

    fun play() {
        if (!::controller.isInitialized) {
            controllerFuture.addListener({ play() }, MoreExecutors.directExecutor())
        } else {
            controller.play()
        }
    }

    fun pause() {
        if (!::controller.isInitialized) {
            controllerFuture.addListener({ pause() }, MoreExecutors.directExecutor())
        } else {
            controller.pause()
        }
    }

    fun seek(to: Long) {
        if (!::controller.isInitialized) {
            controllerFuture.addListener({ seek(to) }, MoreExecutors.directExecutor())
        } else {
            controller.seekTo(to)
            if (!controller.isPlaying) {
                controller.play()
            }
        }
    }

    private fun getMediaItemFromUri(uri: String): MediaItem {
        return MediaItem.fromUri(uri)
            .buildUpon()
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setArtworkUri(Uri.parse(FEELBEAT_LOGO_URL))
                    .setTitle("Guess the song")
                    .build()
            )
            .build()
    }
}
