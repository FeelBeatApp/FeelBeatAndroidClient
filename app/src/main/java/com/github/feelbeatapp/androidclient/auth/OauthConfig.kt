package com.github.feelbeatapp.androidclient.auth

data class OauthConfig(
    val clientId: String,
    val redirectUri: String,
    val scope: String,
    /** Uri to fetch authorization code from */
    val authorizeUri: String,
    /** Uri to exchange code for token */
    val tokenUri: String,
)
