package com.example.primerapplication.authentication

import android.content.Context
import com.primer.TokenUtils
import com.primer.checkSuccessful

class TokenProvider(private val stagingApi: StagingApi, private val context: Context) {

    var token: TokenResponse? = null

    @Synchronized
    fun getOrRenewToken(): String {
        if (token == null) {
            token = PrefsToken(context).getToken()
        }
        return if (token?.clientToken == null || !TokenUtils.isStillValid(token!!.expirationDate)) {
            refreshToken()
        } else token!!.clientToken
    }

    @Synchronized
    private fun refreshToken(): String {
        return try {
            val response = stagingApi.getToken().execute().checkSuccessful()
            PrefsToken(context).saveToken(response.body()!!)
            return response.body()!!.clientToken
        } catch (e: Exception) {
            token = null
            throw e
        }
    }
}
