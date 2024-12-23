package com.github.feelbeatapp.androidclient.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.feelbeatapp.androidclient.R

@Composable
fun LoginScreen(onLoggedIn: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(stringResource(R.string.login_screen))
        Button(onClick = onLoggedIn) { Text(stringResource(R.string.login_with_spotify)) }
    }
}
