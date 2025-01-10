package com.github.feelbeatapp.androidclient.ui.app.game.guesssong.components

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@SuppressWarnings("MagicNumber")
class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _currentPosition = MutableStateFlow(0f)
    val currentPosition: StateFlow<Float> get() = _currentPosition

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    val duration: Float
        get() = maxOf(exoPlayer.duration.toFloat(), 0f)

    init {
        exoPlayer.prepare()
    }

    fun loadSong(songUrl: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(songUrl))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun play() {
        exoPlayer.play()
        _isPlaying.value = true
        trackPosition()
    }

    fun pause() {
        exoPlayer.pause()
        _isPlaying.value = false
    }

    fun seekTo(position: Float) {
        exoPlayer.seekTo(position.toLong())
        _currentPosition.value = position
    }

    private fun trackPosition() {
        viewModelScope.launch {
            while (_isPlaying.value) {
                _currentPosition.value = exoPlayer.currentPosition.toFloat()
                delay(500)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}