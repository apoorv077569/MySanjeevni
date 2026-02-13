package com.mysanjeevni.mysanjeevni.di

import com.mysanjeevni.mysanjeevni.core.Constants
import com.mysanjeevni.mysanjeevni.features.pharmacy.data.remote.PharmacyApi
import dagger.Module
import dagger.Provides
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
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun providePharmacyApi(retofit: Retrofit): PharmacyApi{
        return retofit.create(PharmacyApi::class.java)
    }
}