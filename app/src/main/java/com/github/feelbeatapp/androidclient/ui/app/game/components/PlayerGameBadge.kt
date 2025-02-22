package com.github.feelbeatapp.androidclient.ui.app.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.game.model.GuessCorrectness
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

const val ICON_SIZE = .8f

@Composable
fun PlayerGameBadge(
    imageUrl: String,
    points: Int? = null,
    result: GuessCorrectness = GuessCorrectness.UNKNOWN,
    size: Dp = 80.dp,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(modifier = Modifier.size(size).clip(CircleShape)) {
            AsyncImage(
                model = imageUrl,
                error = painterResource(R.drawable.account),
                placeholder = painterResource(R.drawable.account),
                contentDescription = stringResource(R.string.player_image),
                modifier =
                    Modifier.size(size)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            )

            val color =
                when (result) {
                    GuessCorrectness.CORRECT -> MaterialTheme.colorScheme.primary.copy(alpha = .7f)
                    GuessCorrectness.INCORRECT ->
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = .7f)
                    else -> Color.Transparent
                }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(color),
            ) {
                if (result == GuessCorrectness.CORRECT) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = "correct",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxSize(ICON_SIZE),
                    )
                } else if (result == GuessCorrectness.INCORRECT) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "incorrect",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxSize(ICON_SIZE),
                    )
                }
            }
        }
        if (points != null) {
            Text(
                text = points.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PlayerBadgePreview() {
    FeelBeatTheme {
        PlayerGameBadge(
            imageUrl = "https://cdn-icons-png.flaticon.com/512/219/219983.png",
            points = 200,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PlayerBadgeCorrectPreview() {
    FeelBeatTheme {
        PlayerGameBadge(
            imageUrl = "https://cdn-icons-png.flaticon.com/512/219/219983.png",
            points = 200,
            result = GuessCorrectness.CORRECT,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PlayerBadgeIncorrectPreview() {
    FeelBeatTheme {
        PlayerGameBadge(
            imageUrl = "https://cdn-icons-png.flaticon.com/512/219/219983.png",
            points = 200,
            result = GuessCorrectness.INCORRECT,
        )
    }
}
