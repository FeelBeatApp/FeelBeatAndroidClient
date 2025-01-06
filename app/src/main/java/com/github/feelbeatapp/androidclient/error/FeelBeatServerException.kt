package com.github.feelbeatapp.androidclient.error

class FeelBeatServerException(val serverMessage: String) :
    FeelBeatException(ErrorCode.FEELBEAT_SERVER_ERROR)
