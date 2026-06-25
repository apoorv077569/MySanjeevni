package com.mysanjeevni.mysanjeevni.data.remote


import com.google.gson.GsonBuilder
import com.mysanjeevni.mysanjeevni.core.Constants.AUTH_BASE_URL
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AuthApiClient {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val gson = GsonBuilder()
        .create()

    val api: AuthApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuthApiService::class.java)
    }
}