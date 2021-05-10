package com.example.primerapplication

import android.content.Context
import com.example.primerapplication.authentication.STAGING_URL_API
import com.example.primerapplication.authentication.StagingApi
import com.example.primerapplication.authentication.StagingInterceptor
import com.example.primerapplication.authentication.TokenProvider
import com.google.gson.GsonBuilder
import com.primer.PRIMER_URL_API
import com.primer.PrimerApi
import com.primer.PrimerAuthInterceptor
import com.primer.TokenizedPaymentDeserializer
import com.primer.TokenizedPaymentInstrumentInfo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiBuilders {

    fun getPrimerApi(context: Context): PrimerApi {
        val customGson = GsonBuilder().registerTypeAdapter(TokenizedPaymentInstrumentInfo::class.java, TokenizedPaymentDeserializer()).create()
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        PrimerAuthInterceptor(
                            TokenProvider(getStagingApi(), context).getOrRenewToken()
                        )
                    ).build()
            )
            .baseUrl(PRIMER_URL_API)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create(PrimerApi::class.java)
    }

    fun getStagingApi(): StagingApi {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(StagingInterceptor(BuildConfig.PRIMER_API_KEY))
                    .build()
            )
            .baseUrl(STAGING_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StagingApi::class.java)
    }
}