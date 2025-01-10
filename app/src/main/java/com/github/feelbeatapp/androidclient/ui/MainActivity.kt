package com.github.feelbeatapp.androidclient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.feelbeatapp.androidclient.infra.auth.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startRoute = if (authManager.isAuthenticated()) RootRoute.APP else RootRoute.LOGIN

        enableEdgeToEdge()
        setContent { FeelBeatApp(startRoute) }
    }
}
