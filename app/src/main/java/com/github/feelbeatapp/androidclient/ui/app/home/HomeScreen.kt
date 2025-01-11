package com.github.feelbeatapp.androidclient.ui.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.api.feelbeat.responses.RoomListViewResponse
import com.github.feelbeatapp.androidclient.ui.app.components.RoomCard

@Composable
fun HomeScreen(
    onRoomSelect: (String) -> Unit,
    onNewRoom: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val rooms by homeViewModel.rooms.collectAsState()
    val loading by homeViewModel.loading.collectAsState()

    LaunchedEffect(null) {
        homeViewModel.leaveRoom()
        homeViewModel.loadRooms()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.current_games),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
            )

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(50.dp),
                    )
                }
            } else {
                RoomList(
                    items = rooms,
                    isRefreshing = loading,
                    onRefresh = { homeViewModel.loadRooms() },
                    onRoomSelect = onRoomSelect,
                    modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                )
            }
        }

        IconButton(
            onClick = onNewRoom,
            modifier =
                Modifier.align(Alignment.BottomEnd)
                    .padding(bottom = 40.dp, end = 40.dp)
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium,
                    ),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(36.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomList(
    items: List<RoomListViewResponse>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onRoomSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh, modifier = modifier) {
        LazyColumn(Modifier) {
            items(items) { ListItem({ RoomCard(room = it, onClick = { onRoomSelect(it.id) }) }) }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(onRoomSelect = {}, onNewRoom = {})
}
