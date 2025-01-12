package com.github.feelbeatapp.androidclient.ui.app.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.api.feelbeat.responses.RoomListViewResponse

@Composable
fun RoomCard(
    room: RoomListViewResponse,
    onClick: () -> Unit,
    elevation: Dp = 4.dp,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier =
            modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(8.dp)
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small,
                ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 0.dp),
        ) {
            AsyncImage(
                model = room.imageUrl,
                contentDescription = stringResource(R.string.playlist_image),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.account),
                error = painterResource(R.drawable.account),
                modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.small),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "Players:  ${room.players}/${room.maxPlayers}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
fun RoomCardPreview() {
    val mockRoom =
        RoomListViewResponse(
            id = "1",
            name = "Chill Vibes Room",
            imageUrl = "https://upload.wikimedia.org/wikipedia/it/2/20/Aerials.png",
            players = 2,
            maxPlayers = 4,
        )

    MaterialTheme { RoomCard(room = mockRoom, onClick = { /* Do nothing */ }) }
}
