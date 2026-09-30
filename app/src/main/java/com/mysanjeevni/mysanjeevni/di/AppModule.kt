package com.mysanjeevni.mysanjeevni.di

import android.app.Application
import androidx.room.Room
import com.mysanjeevni.mysanjeevni.core.Constants
import com.mysanjeevni.mysanjeevni.features.cart.data.local.dao.CartDao
import com.mysanjeevni.mysanjeevni.data.local.dao.NotificationDao
import com.mysanjeevni.mysanjeevni.data.local.db.AppDatabase
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.features.labs.data.remote.LabPaymentApi
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.PaymentApi
import com.mysanjeevni.mysanjeevni.features.pharmacy.data.remote.PharmacyApi
import com.mysanjeevni.mysanjeevni.features.profile.data.remote.AddressApiClient
import com.mysanjeevni.mysanjeevni.features.profile.data.remote.AddressApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @MainRetrofit
    fun provideMainRetrofit(
        client: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constants.MAIN_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(
        @AuthRetrofit retrofit: Retrofit
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiService(
        @MainRetrofit retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePharmacyApi(
        @MainRetrofit retrofit: Retrofit
    ): PharmacyApi {
        return retrofit.create(PharmacyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAddressApi(): AddressApiService {
        return AddressApiClient.api
    }

    @Provides
    @Singleton
    fun provideCartDatabase(
        app: Application
    ): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "mysanjeevni_db"
        )
            .addMigrations(
                AppDatabase.MIGRATION_4_5
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideCartDao(db: AppDatabase): CartDao {
        return db.cartDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(db: AppDatabase): NotificationDao{
        return db.notificationDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .protocols(listOf(Protocol.HTTP_1_1))
            .addInterceptor(logging)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .callTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun providePaymentApi(@MainRetrofit retrofit: Retrofit): PaymentApi {
        return retrofit.create(PaymentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLabPaymentApi(@AuthRetrofit retrofit: Retrofit): LabPaymentApi {
        return retrofit.create(LabPaymentApi::class.java)
    }

}