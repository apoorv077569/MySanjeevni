package com.mysanjeevni.mysanjeevni.features.profile.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import com.mysanjeevni.mysanjeevni.core.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AddressApiClient {

    private val debugInterceptor = Interceptor { chain ->

        val request = chain.request()

        Log.d(
            "RAW_HTTP",
            """
            ================================
            REQUEST
            METHOD : ${request.method}
            URL    : ${request.url}
            HEADERS:
            ${request.headers}
            BODY   : ${request.body}
            ================================
            """.trimIndent()
        )

        try {

            val response = chain.proceed(request)

            val responseBody = response.peekBody(Long.MAX_VALUE)

            Log.d(
                "RAW_HTTP",
                """
                ================================
                RESPONSE
                CODE    : ${response.code}
                MESSAGE : ${response.message}
                URL     : ${response.request.url}

                HEADERS:
                ${response.headers}

                BODY:
                ${responseBody.string()}
                ================================
                """.trimIndent()
            )

            response

        } catch (e: Exception) {

            Log.e(
                "RAW_HTTP",
                "NETWORK ERROR | URL=${request.url} | ${e.message}",
                e
            )

            throw e
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(debugInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder().create()

    val api: AddressApiService by lazy {

        Log.d(
            "RAW_HTTP",
            "🔥 AddressApiClient.api CREATED"
        )

        Log.d(
            "RAW_HTTP",
            "🔥 COUNTRY_BASE_URL = ${Constants.COUNTRY_BASE_URL}"
        )

        Retrofit.Builder()
            .baseUrl(Constants.COUNTRY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            )
            .build()
            .create(AddressApiService::class.java)
    }
}