package com.example.common.data.api.interceptor

import com.example.common.data.api.ApiConstants.AUTH_ENDPOINT
import com.example.common.data.api.ApiConstants.KEY
import com.example.common.data.api.ApiConstants.SECRET
import com.example.common.data.api.ApiParameters.AUTH_HEADER
import com.example.common.data.api.ApiParameters.CLIENT_ID
import com.example.common.data.api.ApiParameters.CLIENT_SECRET
import com.example.common.data.api.ApiParameters.GRANT_TYPE_KEY
import com.example.common.data.api.ApiParameters.GRANT_TYPE_VALUE
import com.example.common.data.api.ApiParameters.TOKEN_TYPE
import com.example.common.data.preferences.Preferences
import com.example.common.data.api.model.ApiToken
import com.squareup.moshi.Moshi
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.threeten.bp.Instant
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val preferences: Preferences
) : Interceptor {

    companion object {
        const val UNAUTHORIZED = 401
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferences.getToken()
        val tokenExpirationTime = Instant.ofEpochSecond(preferences.getTokenExpirationTime())
        val request = chain.request()

        val interceptedRequest: Request

        if (isValidToken(tokenExpirationTime)) {
            interceptedRequest = chain.createAuthenticatedRequest(token)
        } else {
            val tokenRefreshResponse = chain.refreshToken()

            interceptedRequest = if (tokenRefreshResponse.isSuccessful) {
                val newToken = mapToken(tokenRefreshResponse)

                if (newToken.isValid()) {
                    storeNewToken(newToken)
                    chain.createAuthenticatedRequest(newToken.accessToken!!)
                } else {
                    request
                }
            } else {
                request
            }
        }

        return chain.proceedDeletingTokenIfUnauthorized(interceptedRequest)
    }

    private fun isValidToken(tokenExpirationTime: Instant) =
        tokenExpirationTime.isAfter(Instant.now())

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
        return request()
            .newBuilder()
            .addHeader(AUTH_HEADER, TOKEN_TYPE + token)
            .build()
    }

    private fun Interceptor.Chain.refreshToken(): Response {
        val url = request()
            .url
            .newBuilder(AUTH_ENDPOINT)!!
            .build()

        val body = FormBody.Builder()
            .add(GRANT_TYPE_KEY, GRANT_TYPE_VALUE)
            .add(CLIENT_ID, KEY)
            .add(CLIENT_SECRET, SECRET)
            .build()

        val tokenRefresh = request()
            .newBuilder()
            .post(body)
            .url(url)
            .build()

        return proceedDeletingTokenIfUnauthorized(tokenRefresh)
    }

    private fun Interceptor.Chain.proceedDeletingTokenIfUnauthorized(request: Request): Response {
        val response = proceed(request)

        if (response.code == UNAUTHORIZED) {
            preferences.deleteTokenInfo()
        }

        return response
    }

    private fun mapToken(tokenRefreshResponse: Response): ApiToken {
        val moshi = Moshi.Builder().build()
        val tokenAdapter = moshi.adapter(ApiToken::class.java)
        val responseBody = tokenRefreshResponse.body

        return responseBody?.string()?.let { tokenAdapter.fromJson(it) } ?: ApiToken.INVALID
    }

    private fun storeNewToken(apiToken: ApiToken) {
        with(preferences) {
            putTokenType(apiToken.tokenType!!)
            putTokenExpirationTime(apiToken.expiresAt)
            putToken(apiToken.accessToken!!)
        }
    }
}
