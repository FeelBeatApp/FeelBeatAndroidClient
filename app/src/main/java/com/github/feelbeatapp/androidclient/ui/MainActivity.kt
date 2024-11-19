package com.github.feelbeatapp.androidclient.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.feelbeatapp.androidclient.websocket.WebsocketClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ws()
        enableEdgeToEdge()
        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            FeelBeatApp(widthSizeClass)
        }
    }

    private fun ws() {
        val socket = WebsocketClient()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    socket.connect(
                        host = "10.0.2.2",
                        port = 3000,
                        path = "/ws"
                    )
                }

                launch {
                    while (!socket.isConnected) {
                        delay(10);
                    }

                    for (i in 1..10) {
                        socket.sendMessage(String.format("Message nr %d from client", i))
                        delay(1000)
                    }
                }

                Log.d("test", "it's lanuched")
            }
        }
    }
}