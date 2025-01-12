package com.github.feelbeatapp.androidclient.ui.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun SongCard(
    title: String,
    artist: String,
    imageUrl: String,
    duration: Duration,
    size: Dp,
    elevation: Dp = 6.dp,
    displayDuration: Boolean = true,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        modifier = modifier.height(size).fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.height(size).width(size)) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.song_cover),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.account),
                    error = painterResource(R.drawable.account),
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(10.dp, 0.dp),
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(title, style = MaterialTheme.typography.titleMedium)

                    Text(artist, style = MaterialTheme.typography.titleSmall)
                }

                if (displayDuration) Text(duration.toString())
            }
        }
    }
}

@Preview
@Composable
fun SongCardPreview() {
    FeelBeatTheme {
        SongCard(
            title = "Aerials",
            artist = "System of a down",
            imageUrl = "https://upload.wikimedia.org/wikipedia/it/2/20/Aerials.png",
            duration = 320.seconds,
            size = 60.dp,
        )
    }
}
