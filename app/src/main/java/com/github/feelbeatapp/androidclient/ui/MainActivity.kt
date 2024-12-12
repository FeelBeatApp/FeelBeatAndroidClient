package com.github.feelbeatapp.androidclient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.auth.AuthState
import com.github.feelbeatapp.androidclient.network.fullduplex.NetworkAgent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var socket: NetworkAgent
    @Inject lateinit var authManager: AuthManager

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        socket.connect("/ws")

        val startRoute =
            if (authManager.isAuthenticated()) FeelBeatRoute.HOME
            else FeelBeatRoute.LOGIN

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            FeelBeatApp(widthSizeClass, startRoute)
        }
    }
}
