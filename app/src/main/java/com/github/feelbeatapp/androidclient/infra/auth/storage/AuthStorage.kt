package com.github.feelbeatapp.androidclient.infra.auth.storage

import com.github.feelbeatapp.androidclient.infra.auth.AuthData

interface AuthStorage {
    fun storeAuthData(data: AuthData)

    fun retrieveAuthData(): AuthData?

    fun clearAuthData()
}
