package com.github.feelbeatapp.androidclient.ui.guesssong.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import com.github.feelbeatapp.androidclient.R
import com.github.feelbeatapp.androidclient.model.Song

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun MusicPlayer(song: Song?) {
    val context = LocalContext.current
    val songUrl =
        "https://rr8---sn-u2oxu-3ufd.googlevideo.com/videoplayback?expire=1736227337&ei=qWV8Z9O7CPPw6dsP3dzlqQs&ip=46.134.117.39&id=o-AMJAvgrpT2F_-IKj-8gm03NvT1-pgZ7td-7tns2oLJFs&itag=251&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&met=1736205737%2C&mh=7c&mm=31%2C29&mn=sn-u2oxu-3ufd%2Csn-u2oxu-f5fer&ms=au%2Crdu&mv=m&mvi=8&pl=19&rms=au%2Cau&initcwndbps=1256250&bui=AfMhrI_ANuAQZrgamai7qkOOaLBJnLauQdDFaFj7K9fCmpnRY68wSsujhfa6mmU0XTNJsDQrziOmu1Uh&vprv=1&svpuc=1&mime=audio%2Fwebm&ns=tME3971W2qv7CcvQ1LtAiD8Q&rqh=1&gir=yes&clen=3437753&dur=212.061&lmt=1717047822556748&mt=1736205414&fvip=5&keepalive=yes&fexp=51326932%2C51331020%2C51335594%2C51371294&c=MWEB&sefc=1&txp=4532434&n=izJLsCG11cJHFA&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Csvpuc%2Cmime%2Cns%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=met%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Crms%2Cinitcwndbps&lsig=AGluJ3MwRgIhAJZbP_JrXespvChIW4cWg4LQ5uci5GK8MVztkrJuB4BAAiEA0RRWtoIU7EdxiVQg2Ncvzaf4jKTMg-vbOEZxOn5sHGc%3D&sig=AJfQdSswRAIgOAA4bGhVvic-Tw-aTg2kP4J2x-BsakQ9uKh_PSy4MmMCIDi5EdC7zSvOIPPNJdFvCbx2aQVH6CTkgB0bE9SOlxuu"

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val fileUri = Uri.parse(songUrl)
            val mediaItem = MediaItem.fromUri(fileUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(key1 = exoPlayer) { onDispose { exoPlayer.release() } }

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.music_control),
            style = MaterialTheme.typography.bodyMedium,
        )

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { PlayerControlView(context).apply { player = exoPlayer } },
        )
    }
}
