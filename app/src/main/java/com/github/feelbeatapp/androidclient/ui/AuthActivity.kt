package com.github.feelbeatapp.androidclient.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.ui.login.AuthLoadingScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    @Inject lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val intentUri = Uri.parse(intent.dataString)
        val error = intentUri.getQueryParameter("error")
        val code = intentUri.getQueryParameter("code")

        Log.d("code", code.toString())
        Log.d("error", error.toString())

        if (error != null) {
            startActivity(Intent(this, MainActivity::class.java))
            return
        }

        Log.d("check", authManager.toString())
        val activity = this
        CoroutineScope(Dispatchers.IO).launch {
            authManager.fetchAccessToken(checkNotNull(code))
            activity.startActivity(
                Intent(activity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }

        setContent { AuthLoadingScreen() }
    }
}
