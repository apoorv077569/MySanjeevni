package com.mysanjeevni.mysanjeevni.di

import com.mysanjeevni.mysanjeevni.features.pharmacy.data.repository.MockPharmacyRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
 abstract class RepositoryModule {
     @Binds
     @Singleton
     abstract fun bindPharmacyRepository(
         mockRepo: MockPharmacyRepositoryImpl
     ): PharmacyRepository
}