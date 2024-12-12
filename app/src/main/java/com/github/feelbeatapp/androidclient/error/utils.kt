package com.github.feelbeatapp.androidclient.error

import android.content.Context
import com.github.feelbeatapp.androidclient.R

fun errorCodeToStringResource(context: Context, code: ErrorCode): String {
    return when (code) {
        ErrorCode.AUTHORIZATION_SERVER_UNREACHABLE ->
            context.getString(R.string.authorization_server_unreachable)
        else -> " sfsdf"
    }
}
