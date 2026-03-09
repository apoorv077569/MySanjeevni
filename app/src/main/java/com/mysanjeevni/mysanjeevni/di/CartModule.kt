package com.mysanjeevni.mysanjeevni.di

import com.mysanjeevni.mysanjeevni.features.cart.data.repository.RoomCartRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartModule {
    @Binds
    @Singleton
    abstract fun bindCartRepository(roomRepo: RoomCartRepositoryImpl): CartRepository
}