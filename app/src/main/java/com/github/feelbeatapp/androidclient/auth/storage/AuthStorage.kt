package com.github.feelbeatapp.androidclient.auth.storage

import com.github.feelbeatapp.androidclient.auth.AuthData

interface AuthStorage {
    fun storeAuthData(data: AuthData)

    fun retrieveAuthData(): AuthData?
}
