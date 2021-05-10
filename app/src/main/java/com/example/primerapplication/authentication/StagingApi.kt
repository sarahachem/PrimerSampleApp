package com.example.primerapplication.authentication

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.POST

const val STAGING_URL_API = "https://api.staging.primer.io"

interface StagingApi {

    @POST("/auth/client-token")
    fun getToken(): Call<TokenResponse>
}

class TokenResponse(
        val clientToken: String,
        val expirationDate: String)

class StagingInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().addHeader("X-Api-Key", apiKey).addHeader("Content-Type", "application/json").build())
    }
}
