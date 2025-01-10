package com.github.feelbeatapp.androidclient.infra.error

open class FeelBeatException(val code: ErrorCode, debugMessage: String?, cause: Throwable?) :
    Exception(debugMessage, cause) {
    constructor(code: ErrorCode, debugMessage: String) : this(code, debugMessage, null)

    constructor(code: ErrorCode) : this(code, null, null)

    constructor(code: ErrorCode, cause: Throwable) : this(code, cause.message, cause)
}
