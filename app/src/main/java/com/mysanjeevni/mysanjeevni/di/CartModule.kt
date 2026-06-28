package com.mysanjeevni.mysanjeevni.di

import com.mysanjeevni.mysanjeevni.features.cart.data.local.dao.CartDao
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {
    @Provides
    @Singleton
    fun provideCartRepository(
        api: ApiService,
        dao: CartDao
    ): CartRepository {
        return CartRepository(api,dao)
    }
}