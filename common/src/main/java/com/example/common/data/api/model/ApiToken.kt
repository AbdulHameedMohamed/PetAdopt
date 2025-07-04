package com.example.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class ApiToken(
    @field:Json(name = "token_type") val tokenType: String?,
    @field:Json(name = "expires_in") val expiresInSeconds: Int?,
    @field:Json(name = "access_token") val accessToken: String?
) {
    companion object {
        // Null Object Pattern
        val INVALID = ApiToken("", -1, "")
    }

    @Transient
    private val requestedAt: Instant = Instant.now()

    val expiresAt: Long
        get() {
            if (expiresInSeconds == null) return 0L

            return requestedAt.plusSeconds(expiresInSeconds.toLong()).epochSecond
        }

    fun isValid(): Boolean {
        return !tokenType.isNullOrEmpty() &&
                expiresInSeconds != null && expiresInSeconds >= 0 &&
                !accessToken.isNullOrEmpty()
    }

}
