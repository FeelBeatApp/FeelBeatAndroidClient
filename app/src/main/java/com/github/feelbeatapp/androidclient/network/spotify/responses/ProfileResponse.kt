package com.github.feelbeatapp.androidclient.network.spotify.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class ProfileImage(val url: String, val height: Int, val width: Int)

@Serializable
data class ProfileResponse(
    @SerialName("display_name") val displayName: String,
    val email: String,
    val images: Array<ProfileImage>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProfileResponse

        if (displayName != other.displayName) return false
        if (email != other.email) return false
        if (!images.contentEquals(other.images)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = displayName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + images.contentHashCode()
        return result
    }
}
