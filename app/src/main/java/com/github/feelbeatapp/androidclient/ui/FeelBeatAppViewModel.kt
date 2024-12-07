package com.github.feelbeatapp.androidclient.ui

import androidx.lifecycle.ViewModel
import com.github.feelbeatapp.androidclient.auth.AuthManager
import com.github.feelbeatapp.androidclient.ui.theme.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class FeelBeatAppViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {
    private val _appState = MutableStateFlow(AppState(authManager.isAuthenticated()))
    val appState = _appState.asStateFlow()

    fun example() {
        _appState.update { AppState(false) }
    }
}
