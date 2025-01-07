package com.github.feelbeatapp.androidclient.ui.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.model.RoomListView

@Composable
fun HomeScreen(
    onRoomSelect: (String) -> Unit,
    onNewRoom: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val rooms by homeViewModel.rooms.collectAsState()
    val loading by homeViewModel.loading.collectAsState()

    LaunchedEffect(null) { homeViewModel.loadRooms() }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.current_games),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
        )

        if (loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 4.dp,
                modifier = Modifier.width(50.dp).height(50.dp),
            )
        } else {
            RoomList(
                items = rooms,
                isRefreshing = loading,
                onRefresh = { homeViewModel.loadRooms() },
                onRoomSelect = onRoomSelect,
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).padding(horizontal = 16.dp)
        ) {
            Box(
                modifier =
                    Modifier.align(Alignment.BottomEnd)
                        .size(60.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium,
                        )
            ) {
                IconButton(onClick = onNewRoom, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomList(
    items: List<RoomListView>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onRoomSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh, modifier = modifier) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(items) { ListItem({ RoomItem(room = it, onClick = { onRoomSelect(it.id) }) }) }
        }
    }
}

@Composable
fun RoomItem(room: RoomListView, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier =
            Modifier.fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium,
                ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(onRoomSelect = {}, onNewRoom = {})
}
