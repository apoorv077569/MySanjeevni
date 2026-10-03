package com.mysanjeevni.mysanjeevni.di


import android.util.Log
import com.mysanjeevni.mysanjeevni.core.Constants
import com.mysanjeevni.mysanjeevni.features.prescription.data.remote.PrescriptionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrescriptionModule {

    private const val TAG = "PRESCRIPTION_DI"

    @Provides
    @Singleton
    @PrescriptionOkHttp
    fun providePrescriptionOkHttpClient(): OkHttpClient {

        Log.d(TAG, "Creating Prescription OkHttpClient")

        val loggingInterceptor =
            HttpLoggingInterceptor { message ->
                Log.d("PRESCRIPTION_HTTP", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @PrescriptionRetrofit
    fun providePrescriptionRetrofit(
        @PrescriptionOkHttp okHttpClient: OkHttpClient
    ): Retrofit {

        Log.d(
            TAG,
            "Creating Prescription Retrofit: ${Constants.PRESCRIPTION_BASE_URL}"
        )

        return Retrofit.Builder()
            .baseUrl(Constants.PRESCRIPTION_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun providePrescriptionApiService(
        @PrescriptionRetrofit retrofit: Retrofit
    ): PrescriptionApiService {

        Log.d(TAG, "Creating Prescription Api Service")

        return retrofit.create(
            PrescriptionApiService::class.java
        )
    }
}