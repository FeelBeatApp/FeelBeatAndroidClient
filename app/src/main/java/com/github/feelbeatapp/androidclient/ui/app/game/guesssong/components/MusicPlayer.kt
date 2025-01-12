package com.github.feelbeatapp.androidclient.ui.app.game.guesssong.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.github.feelbeatapp.androidclient.ui.app.uimodel.Song

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressWarnings("UnusedParameter", "MagicNumber", "MaxLineLength")
@Composable
fun MusicPlayer(song: Song?, viewModel: MusicPlayerViewModel = viewModel()) {
    val songUrl =
        "https://rr1---sn-f5f7kn7z.googlevideo.com/videoplayback?expire=1736640305&ei=0bKCZ8qTKaGP6dsPtp_tmQs&ip=89.64.59.248&id=o-ALE7qEciMqUy10n2Udgb3J3jnuA4vbFMQG1GLtYKkA_M&itag=251&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&met=1736618705%2C&mh=7c&mm=31%2C26&mn=sn-f5f7kn7z%2Csn-4g5ednsl&ms=au%2Conr&mv=m&mvi=1&pl=13&rms=au%2Cau&initcwndbps=3535000&bui=AY2Et-MNwKqMAzX6OxUipsXZOxyIPdH-V6oiuntYe7nv6xnqaNEUPyvy8tC4_H5BtCBOdLJ6CKlggE3S&vprv=1&svpuc=1&mime=audio%2Fwebm&ns=FSxCnq53JRL7JoeBUsNPOKwQ&rqh=1&gir=yes&clen=3437753&dur=212.061&lmt=1717047822556748&mt=1736618225&fvip=3&keepalive=yes&fexp=51326932%2C51331020%2C51335594%2C51353498%2C51371294&c=MWEB&sefc=1&txp=4532434&n=Ll0zIouNSqK6tw&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Csvpuc%2Cmime%2Cns%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=met%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Crms%2Cinitcwndbps&lsig=AGluJ3MwRAIgG_3P45XFb2ga0dbUNLab75Si0KHVpqnzK8DrNvdqgZoCIGLHpIxBH9nZc0J71nV4gkDK7-L7MX3R9hNEaillUJXM&sig=AJfQdSswRgIhAPfijY_GFlfmFZMmZWCovzFhnP8Kpgmdn0RkpbnW14HlAiEAw3-fOda0dA3auJyS7pWBeMlrppg_LjBZRISI-R1pe6w%3D"

    LaunchedEffect(songUrl) { viewModel.loadSong(songUrl) }

    val currentPosition by viewModel.currentPosition.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    val validDuration = viewModel.duration
    val validPosition = currentPosition.coerceIn(0f, validDuration)

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Slider(
            value = validPosition,
            onValueChange = { newValue -> viewModel.seekTo(newValue) },
            valueRange = 0f..validDuration,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { viewModel.seekTo(maxOf(currentPosition - 5000f, 0f)) }) {
                Text(text = "<<")
            }

            Button(
                onClick = {
                    if (isPlaying) {
                        viewModel.pause()
                    } else {
                        viewModel.play()
                    }
                }
            ) {
                Text(text = if (isPlaying) "||" else "▶")
            }

            Button(onClick = { viewModel.seekTo(minOf(currentPosition + 5000f, validDuration)) }) {
                Text(text = ">>")
            }
        }

        Log.d("MusicPlayer", "Position: $currentPosition, Duration: $validDuration")
    }
}
