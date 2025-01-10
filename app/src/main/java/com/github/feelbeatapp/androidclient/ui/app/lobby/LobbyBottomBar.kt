package com.github.feelbeatapp.androidclient.ui.app.lobby

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.navigation.AppRoute
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun LobbyBottomBar(
    activeRoute: AppRoute,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        NavigationBarItem(
            icon = {
                Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.selected_room))
            },
            label = { Text(stringResource(R.string.selected_room)) },
            selected = activeRoute == AppRoute.ROOM_LOBBY,
            onClick = { onNavigate(AppRoute.ROOM_LOBBY) },
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "List icon") },
            label = { Text("Songs") },
            selected = activeRoute == AppRoute.ROOM_SONGS,
            onClick = { onNavigate(AppRoute.ROOM_SONGS) },
        )

        NavigationBarItem(
            icon = {
                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
            },
            label = { Text(stringResource(R.string.settings)) },
            selected = activeRoute == AppRoute.ROOM_EDIT,
            onClick = { onNavigate(AppRoute.ROOM_EDIT) },
        )
    }
}

@Composable
@Preview
fun LobbyBottomBarPreview() {
    FeelBeatTheme { LobbyBottomBar(AppRoute.ROOM_LOBBY, {}) }
}
