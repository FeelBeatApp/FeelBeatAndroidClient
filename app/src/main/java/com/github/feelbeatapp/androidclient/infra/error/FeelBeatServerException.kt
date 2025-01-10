package com.github.feelbeatapp.androidclient.infra.error

class FeelBeatServerException(val serverMessage: String) :
    FeelBeatException(ErrorCode.FEELBEAT_SERVER_ERROR)
