package com.github.feelbeatapp.androidclient.ui.app

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.navigation.AppRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    title: String,
    backVisible: Boolean,
    onLogout: () -> Unit,
    navController: NavController,
    // onNavigateBack: () -> Unit,
    appViewModel: AppViewModel = hiltViewModel(),
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val playerIdentity by appViewModel.playerIdentity.collectAsState()

    var playerIdentitySheetOpen by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog = true
            }
        }
    }

    LaunchedEffect(backPressedDispatcher) { backPressedDispatcher?.addCallback(backCallback) }

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

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = stringResource(R.string.exit_room)) },
            text = {
                Text(
                    text = stringResource(R.string.are_you_sure),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right,
                ) {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            navController.navigate(AppRoute.HOME.route) {
                                popUpTo(AppRoute.HOME.route)
                            }
                            // onNavigateBack()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.yes),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    TextButton(
                        onClick = { showExitDialog = false },
                        modifier =
                            Modifier.background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = MaterialTheme.shapes.small,
                                )
                                .padding(horizontal = 4.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.no),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            },
        )
    }

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        topBar = {
            AppBar(
                title = title,
                backVisible = backVisible,
                imageUrl = playerIdentity?.imageUrl,
                onAccountClick = { playerIdentitySheetOpen = true },
                onBackClick = { showExitDialog = true },
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
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                    .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "log out",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(vertical = 4.dp).clickable { onLogoutClick() },
            )
        }
    }
}

@Composable
@Preview
fun AppScreenPreview() {
    // FeelBeatTheme { AppScreen(title = "Title", backVisible = true, {}, {}) }
}
