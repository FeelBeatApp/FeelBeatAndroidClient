package com.github.feelbeatapp.androidclient.auth.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.github.feelbeatapp.androidclient.auth.AuthData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject

class PreferencesAuthStorage @Inject constructor(@ApplicationContext ctx: Context) : AuthStorage {
    private val preferences =
        EncryptedSharedPreferences.create(
            "auth_data",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    override fun storeAuthData(data: AuthData) {
        preferences
            .edit()
            .putString("access_token", data.accessToken)
            .putString("refresh_token", data.refreshToken)
            .putString("expires", data.expires.toString())
            .apply()
    }

    override fun retrieveAuthData(): AuthData? {
        val accessToken = preferences.getString("access_token", null) ?: return null
        val refreshToken = preferences.getString("refresh_token", null) ?: return null
        val expirationString = preferences.getString("expires", null) ?: return null
        val expires = Instant.parse(expirationString)

        return AuthData(accessToken = accessToken, refreshToken = refreshToken, expires = expires)
    }
}
