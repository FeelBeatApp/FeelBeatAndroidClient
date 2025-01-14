package com.github.feelbeatapp.androidclient.ui.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.game.model.Player
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun PlayerCard(
    player: Player,
    size: Dp = 80.dp,
    modifier: Modifier = Modifier,
    block: @Composable () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(modifier = Modifier.size(size).clip(CircleShape)) {
            AsyncImage(
                model = player.imageUrl,
                error = painterResource(R.drawable.account),
                placeholder = painterResource(R.drawable.account),
                contentDescription = stringResource(R.string.player_image),
                modifier =
                    Modifier.size(size)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            )
            block()
        }
        Text(
            text = player.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PlayerCardReadyPreview() {
    FeelBeatTheme {
        PlayerCard(
            player =
                Player(
                    id = "<id>",
                    name = "Player name",
                    imageUrl = "https://cdn-icons-png.flaticon.com/512/219/219983.png",
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = .8f)),
            ) {
                Text("Ready!", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
