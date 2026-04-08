package com.mysanjeevni.mysanjeevni.data.remote

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://mysanjeevani.onrender.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}