package com.github.feelbeatapp.androidclient.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    title: String,
    backVisible: Boolean,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit,
    appViewModel: AppViewModel = hiltViewModel(),
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val playerIdentity by appViewModel.playerIdentity.collectAsState()

    var playerIdentitySheetOpen by remember { mutableStateOf(false) }

    if (playerIdentitySheetOpen) {
        ModalBottomSheet(onDismissRequest = { playerIdentitySheetOpen = false }) {
            UserAccountBottomSheetContent(
                playerIdentity = playerIdentity,
                onLogoutClick = {
                    appViewModel.logout()
                    playerIdentitySheetOpen = false
                    onLogout()
                },
            )
        }
    }

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        topBar = {
            AppBar(
                title = title,
                backVisible = backVisible,
                imageUrl = playerIdentity?.imageUrl,
                onAccountClick = { playerIdentitySheetOpen = true },
                onBackClick = onNavigateBack,
            )
        },
        snackbarHost = { SnackbarHost(hostState = appViewModel.snackBarHost) },
        bottomBar = { bottomBar() },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) { content() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    backVisible: Boolean,
    imageUrl: String?,
    onAccountClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        actions = {
            IconButton(onClick = onAccountClick) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.player_image),
                    modifier = Modifier.size(80.dp).clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.account),
                    error = painterResource(R.drawable.account),
                )
            }
        },
        navigationIcon = {
            if (backVisible) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }
        },
    )
}

@Composable
fun UserAccountBottomSheetContent(playerIdentity: PlayerIdentity?, onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier =
                Modifier.size(80.dp).background(Color.Gray, shape = MaterialTheme.shapes.large),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = playerIdentity?.imageUrl,
                contentDescription = "Player Image",
                modifier = Modifier.size(80.dp).clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.account),
                error = painterResource(R.drawable.account),
            )
        }
        Text(text = playerIdentity?.name ?: "...", style = MaterialTheme.typography.titleMedium)
        Button(
            onClick = { onLogoutClick() },
            modifier =
                Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
        ) {
            Text(
                text = "log out",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier,
            )
        }
    }
}

@Composable
@Preview
fun AppScreenPreview() {
    // FeelBeatTheme { AppScreen(title = "Title", backVisible = true, {}, {}) }
}
