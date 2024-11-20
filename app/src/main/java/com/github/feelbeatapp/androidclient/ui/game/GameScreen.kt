package com.github.feelbeatapp.androidclient.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun GameScreen(viewModel: GameViewModel = hiltViewModel<GameViewModel>()) {
    val uiState by viewModel.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        TextField(
            value = uiState.textInput,
            onValueChange = { text -> viewModel.setText(text) },
            label = { Text("Synced input") },
        )
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    FeelBeatTheme { GameScreen() }
}
