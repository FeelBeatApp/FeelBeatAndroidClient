package com.github.feelbeatapp.androidclient.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val ctx = LocalContext.current

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier.width(600.dp),
            )

            Row {
                Text(
                    "Feel",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displayLarge,
                )
                Text(
                    "Beat",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayLarge,
                )
            }
        }
        Button(onClick = { loginViewModel.login(ctx) }) {
            Text(
                stringResource(R.string.login_with_spotify),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp, 8.dp),
            )
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    FeelBeatTheme { LoginScreen() }
}
