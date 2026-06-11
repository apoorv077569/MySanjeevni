package com.mysanjeevni.mysanjeevni.di

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.labs.data.repository.LabsRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.LabsRepository
import com.mysanjeevni.mysanjeevni.features.medicines.data.repository.MedicineRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.medicines.domain.repository.MedicineRepository
import com.mysanjeevni.mysanjeevni.features.orders.data.repository.OrderRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.PaymentRepository
import com.mysanjeevni.mysanjeevni.features.payment.domain.repository.PaymentRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.pharmacy.data.repository.MockPharmacyRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import com.mysanjeevni.mysanjeevni.features.review.data.repository.ReviewRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.review.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
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
    @Binds
    @Singleton
    abstract fun bindLabsRepository(
        impl: LabsRepositoryImpl
    ): LabsRepository

    @Binds
    @Singleton
    abstract fun bindMedicineRepository(
        impl: MedicineRepositoryImpl
    ): MedicineRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        impl:ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        impl: PaymentRepositoryImpl
    ): PaymentRepository

}