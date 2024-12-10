package com.github.feelbeatapp.androidclient.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {
    fun login(ctx: Context) {
        authManager.startLoginFlow(ctx)
    }
}
