package com.github.feelbeatapp.androidclient.ui.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.app.model.Room

@Composable
fun HomeScreen(
    onRoomSelect: (Room) -> Unit,
    onNewRoom: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val rooms by homeViewModel.rooms.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.current_games),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(rooms) { room -> RoomItem(room = room, onClick = { onRoomSelect(room) }) }
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

@Composable
fun RoomItem(room: Room, onClick: () -> Unit) {
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
