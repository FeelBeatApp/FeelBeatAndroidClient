package com.github.feelbeatapp.androidclient.ui.app.game.guesssong.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun AudioPlayerControls(
    value: Long,
    onValueChange: (Long) -> Unit,
    duration: Long,
    isPlaying: Boolean,
    onPlayPause: (isPlaying: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(value, isPlaying) {
        if (isPlaying) {
            val d = (duration - value).toInt()
            if (d > 0) {
                animate(
                    initialValue = value.toFloat(),
                    targetValue = duration.toFloat(),
                    animationSpec = tween(durationMillis = d, easing = LinearEasing),
                ) { value, _ ->
                    progress = value
                }
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Slider(
            value = progress,
            onValueChange = { onValueChange(it.toLong()) },
            valueRange = 0f..duration.toFloat(),
            modifier = Modifier.height(32.dp).weight(1f),
        )

        IconButton(onClick = { onPlayPause(isPlaying) }) {
            if (isPlaying) {
                Icon(
                    ImageVector.vectorResource(R.drawable.pause),
                    tint = Color.Black,
                    contentDescription = "play/pause",
                )
            } else {

                Icon(Icons.Default.PlayArrow, contentDescription = "play/pause")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AudioPlayerControlsPreview() {
    FeelBeatTheme {
        AudioPlayerControls(
            value = 2000,
            onValueChange = {},
            duration = 10000,
            isPlaying = true,
            onPlayPause = {},
        )
    }
}
