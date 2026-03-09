package com.mysanjeevni.mysanjeevni.di

import android.app.Application
import androidx.room.Room
import com.mysanjeevni.mysanjeevni.core.Constants
import com.mysanjeevni.mysanjeevni.features.cart.data.local.CartDao
import com.mysanjeevni.mysanjeevni.features.cart.data.local.CartDatabase
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

    @Provides
    @Singleton
    fun provideCartDatabase(app: Application): CartDatabase{
        return Room.databaseBuilder(
            app,
            CartDatabase::class.java,
            "mysanjeevni_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartDao(db: CartDatabase): CartDao{
        return db.cartDao()
    }
}