package com.github.feelbeatapp.androidclient.model

import android.net.Uri

data class Song(val id: Int, val title: String, val songUri: Uri? = null)
