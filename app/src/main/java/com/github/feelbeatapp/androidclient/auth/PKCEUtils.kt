package com.github.feelbeatapp.androidclient.auth

import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class PKCEUtils {
    companion object {
        private const val CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        private const val CODE_VERIFIER_LENGTH = 64
    }

    fun generateCodeVerifier(): String {
        return (1..CODE_VERIFIER_LENGTH).map { CHARSET.random() }.joinToString("")
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getCodeChallenge(code: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(code.toByteArray())

        return Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(hash)
    }
}
