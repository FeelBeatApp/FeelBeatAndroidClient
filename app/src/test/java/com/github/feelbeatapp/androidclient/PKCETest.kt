package com.github.feelbeatapp.androidclient

import com.github.feelbeatapp.androidclient.auth.PKCEUtils
import org.junit.Assert.assertEquals
import org.junit.Test

const val FAKE_CODE = "zwjW35h3c6jrAD1rTncRqZ9I53VZs5pMWOXy5zDzY8GYKkkwVOaAzPnCKdzuTnYx"
const val FAKE_CHALLENGE = "VscSyUiQaUl9-70H8p0X1iZhAnK-LXUPzmeenbgnhgc"

class PKCETest {
    @Test
    fun getCodeChallange_returns_correctBase64encodedSHA256() {
        val pkce = PKCEUtils()

        assertEquals(FAKE_CHALLENGE, pkce.getCodeChallenge(FAKE_CODE))
    }
}
