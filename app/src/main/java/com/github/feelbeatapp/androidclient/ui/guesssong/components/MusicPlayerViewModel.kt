package com.github.feelbeatapp.androidclient.ui.guesssong.components

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.feelbeatapp.androidclient.media.MusicService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@SuppressWarnings("MagicNumber")
class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak") private val context: Context = application.applicationContext

    @SuppressLint("StaticFieldLeak") private var musicService: MusicService? = null
    private val serviceIntent = Intent(context, MusicService::class.java)
    private var serviceConnection: ServiceConnection? = null

    private val _currentPosition = MutableStateFlow(0f)
    val currentPosition: StateFlow<Float> = _currentPosition.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    var duration = 0f

    init {
        bindToService()
    }

    private fun bindToService() {
        serviceConnection =
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val binder = service as MusicService.MusicBinder
                    musicService = binder.getService()
                    observePlayerState()
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    musicService = null
                }
            }

        context.bindService(serviceIntent, serviceConnection!!, Context.BIND_AUTO_CREATE)
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection?.let { context.unbindService(it) }
        serviceConnection = null
    }

    fun loadSong(songUrl: String) {
        musicService?.playSongFromUrl(songUrl)
    }

    private fun observePlayerState() {
        val player = musicService?.getPlayer() ?: return

        viewModelScope.launch {
            while (true) {
                _currentPosition.value = player.currentPosition / 1000f
                _isPlaying.value = player.playWhenReady
                duration = (player.duration / 1000f).coerceAtLeast(0f)
                delay(100)
            }
        }
    }

    fun play() {
        musicService?.getPlayer()?.play()
    }

    fun pause() {
        musicService?.getPlayer()?.pause()
    }

    fun seekTo(position: Float) {
        musicService?.getPlayer()?.seekTo((position * 1000).toLong())
    }
}
