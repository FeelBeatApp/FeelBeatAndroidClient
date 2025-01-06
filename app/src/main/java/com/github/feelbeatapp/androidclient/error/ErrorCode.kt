package com.github.feelbeatapp.androidclient.error

import com.github.feelbeatapp.androidclient.R

enum class ErrorCode {
    AUTHORIZATION_SERVER_UNREACHABLE,
    AUTHORIZATION_ACCESS_TOKEN_ERROR,
    AUTHORIZATION_REFRESH_TOKEN_ERROR,
    AUTHENTICATION_LOGOUT_ERROR,
    FEELBEAT_SERVER_UNREACHABLE,
    FEELBEAT_SERVER_ERROR,
    FEELBEAT_SERVER_INCORRECT_RESPONSE_FORMAT,
    INCORRECT_PLAYLIST_LINK;

    fun toStringId(): Int {
        return when (this) {
            AUTHORIZATION_SERVER_UNREACHABLE -> R.string.authorization_server_unreachable
            FEELBEAT_SERVER_UNREACHABLE -> R.string.feelbeat_server_unreachable
            INCORRECT_PLAYLIST_LINK -> R.string.incorrect_playlist_link
            FEELBEAT_SERVER_ERROR -> R.string.feelbeat_server_error
            else -> R.string.unexpected_error
        }
    }
}
