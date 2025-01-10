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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.game.model.Song

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressWarnings("UnusedParameter", "MagicNumber", "MaxLineLength")
@Composable
fun MusicPlayer(song: Song?, viewModel: MusicPlayerViewModel = viewModel()) {
    val songUrl =
        "https://rr1---sn-f5f7kn7z.googlevideo.com/videoplayback?expire=1736382209&ei=ocJ-Z9rDKdzIi9oPttXeGA&ip=89.64.59.110&id=o-AIw5B7UWYt8Cun_FzmFcxT_tnyLYgPNeX7WZJodLv2_R&itag=251&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&met=1736360609%2C&mh=7c&mm=31%2C29&mn=sn-f5f7kn7z%2Csn-f5f7lne6&ms=au%2Crdu&mv=m&mvi=1&pl=13&rms=au%2Cau&initcwndbps=4077500&bui=AfMhrI9H--f893o2V_2CV5AO_Ys3rJ8XrY95NAU0biLhkdO1Zvqy6rJQkax9Kx4rAkR5ZM32t61SlCpB&vprv=1&svpuc=1&mime=audio%2Fwebm&ns=a3LuCJvRUAhr73CwU5RvFd4Q&rqh=1&gir=yes&clen=3437753&dur=212.061&lmt=1717047822556748&mt=1736359998&fvip=2&keepalive=yes&fexp=51326932%2C51331020%2C51335594%2C51353497%2C51371294&c=MWEB&sefc=1&txp=4532434&n=cINe0cNGNUcYOw&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Csvpuc%2Cmime%2Cns%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=met%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Crms%2Cinitcwndbps&lsig=AGluJ3MwRQIgGUmNqetE4KbkqcTSemrtNffEhKyzPMY85Nwta0AW2x0CIQCroYFKAIhJFxt9IXpT5obh1CKb47snNnb0UDdo8P-Tyw%3D%3D&sig=AJfQdSswRQIhAKPcNi4W5TcfwjuZ2v3NaKCjfa_JDML3volNCbF5JSvzAiAe3Ifz0AvCfw4NgSNg99iFsU0UPBMDLcbYzcOHsGEJAQ%3D%3D"

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
        Text(
            text = stringResource(id = R.string.music_control),
            modifier = Modifier.padding(bottom = 8.dp),
        )

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