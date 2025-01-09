package com.github.feelbeatapp.androidclient.ui.guesssong.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.model.Song

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressWarnings("UnusedParameter", "MagicNumber", "MaxLineLength")
@Composable
fun MusicPlayer(song: Song?, viewModel: MusicPlayerViewModel = viewModel()) {

    val songUrl =
        "https://rr2---sn-f5f7lne6.googlevideo.com/videoplayback?expire=1736404228&ei=pBh_Z_-eKqa26dsPiov3oAI&ip=89.64.59.110&id=o-AHrZdGN3ZM-k2s4lC6Nn9bnIqSloX8_sFjs0-R9mLcrM&itag=251&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&met=1736382628%2C&mh=7c&mm=31%2C26&mn=sn-f5f7lne6%2Csn-4g5ednsl&ms=au%2Conr&mv=m&mvi=2&pl=13&rms=au%2Cau&initcwndbps=4412500&bui=AfMhrI8xm-ovPvhq05h8x0XlKeMXcWTx8TLVA8vv5TBsXiMTk62anOBLQbWagFUtfqI-hYQozvvlA5tE&vprv=1&svpuc=1&mime=audio%2Fwebm&ns=7p9qiXiF6TZ1cHGdFI6Fz4sQ&rqh=1&gir=yes&clen=3437753&dur=212.061&lmt=1717047822556748&mt=1736382302&fvip=3&keepalive=yes&fexp=51326932%2C51331020%2C51335594%2C51353498%2C51358316%2C51371294&c=MWEB&sefc=1&txp=4532434&n=zDCpa8mvFvvWEw&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Csvpuc%2Cmime%2Cns%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=met%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Crms%2Cinitcwndbps&lsig=AGluJ3MwRQIhANAtvxm07vbZ9wgnPSYCtzyZWGnkhTC4Zthovh1ybMREAiApYGy59CM3Pml_EoBI5en46ZGG0qaFK_Y5l_YQEkcz4Q%3D%3D&sig=AJfQdSswRQIgLr57DYm4atVKmAF0tfBnmFGVRGmlaszGvab1-WQoHVkCIQCgozRkIp6N8Leaq0drV8IxyJR72Mq3uJgMEsAV4nTOnw%3D%3D"

    LaunchedEffect(song) {
        viewModel.loadSong(songUrl)
    }

    val currentPosition by viewModel.currentPosition.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    val validDuration = viewModel.duration
    val validPosition = currentPosition.coerceIn(0f, validDuration)

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.music_control),
            modifier = Modifier.padding(bottom = 8.dp),
        )

        if (validDuration > 0) {
            Slider(
                value = validPosition,
                onValueChange = { newValue -> viewModel.seekTo(newValue) },
                valueRange = 0f..validDuration,
            )
        } else {
            Text("Loading...")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { viewModel.seekTo(maxOf(currentPosition - 5f, 0f)) }) {
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

            Button(onClick = { viewModel.seekTo(minOf(currentPosition + 5f, validDuration)) }) {
                Text(text = ">>")
            }
        }

        Log.d("MusicPlayer", "Position: $currentPosition, Duration: $validDuration")
    }
}