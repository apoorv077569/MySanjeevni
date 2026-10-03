package com.mysanjeevni.mysanjeevni.di

import com.mysanjeevni.mysanjeevni.features.auth.data.repository.AuthRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.auth.data.repository.GoogleAuthRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.AuthRepository
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.GoogleAuthRepository
import com.mysanjeevni.mysanjeevni.features.category.data.repository.CategoryRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.category.domain.repository.CategoryRepository
import com.mysanjeevni.mysanjeevni.features.consult.data.repository.AgoraRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.consult.data.repository.ConsultRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.AgoraRepository
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.ConsultRepository
import com.mysanjeevni.mysanjeevni.features.currency.data.repository.CurrencyRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.currency.domain.repository.CurrencyRepository
import com.mysanjeevni.mysanjeevni.features.home.data.repository.LocationRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.home.domain.repository.LocationRepository
import com.mysanjeevni.mysanjeevni.features.labs.data.repository.BookingHistoryRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.labs.data.repository.LabsRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.BookingHistoryRepository
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.LabsRepository
import com.mysanjeevni.mysanjeevni.features.medicines.data.repository.MedicineRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.medicines.domain.repository.MedicineRepository
import com.mysanjeevni.mysanjeevni.features.orders.data.repository.OrderRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.orders.data.repository.OrderSmsRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderSmsRepository
import com.mysanjeevni.mysanjeevni.features.payment.domain.repository.PaymentRepository
import com.mysanjeevni.mysanjeevni.features.payment.data.repository.PaymentRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.pharmacy.data.repository.MockPharmacyRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import com.mysanjeevni.mysanjeevni.features.prescription.data.repository.PrescriptionRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.prescription.domain.repository.PrescriptionRepository
import com.mysanjeevni.mysanjeevni.features.profile.data.repository.AddressRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.profile.data.repository.ProfileRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.ProfileRepository
import com.mysanjeevni.mysanjeevni.features.review.data.repository.ReviewRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.review.domain.repository.ReviewRepository
import com.mysanjeevni.mysanjeevni.features.support.chat.data.repository.ChatRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.support.chat.domain.repository.ChatRepository
import com.mysanjeevni.mysanjeevni.features.support.returns.data.repository.ReturnRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.repository.ReturnRepository
import com.mysanjeevni.mysanjeevni.features.support.ticket.data.repository.SupportRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.repository.SupportRepository
import com.mysanjeevni.mysanjeevni.features.wishlist.data.repository.WishlistRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.repository.WishlistRepository
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

    @Binds
    @Singleton
   abstract fun bindWishlistRepository(
        impl: WishlistRepositoryImpl
    ): WishlistRepository

   @Binds
   @Singleton
   abstract fun bindPrescriptionRepository(
       impl: PrescriptionRepositoryImpl
   ): PrescriptionRepository

   @Binds
   @Singleton
   abstract fun bindCategoryRepository(
       impl: CategoryRepositoryImpl
   ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBookingHistoryRepository(
        impl: BookingHistoryRepositoryImpl
    ): BookingHistoryRepository

    @Binds
    @Singleton
    abstract fun bindSupportRepository(
        impl: SupportRepositoryImpl
    ): SupportRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindReturnRepository(
        impl: ReturnRepositoryImpl
    ): ReturnRepository

    @Binds
    @Singleton
    abstract fun bindGoogleAuthRepository(
        impl: GoogleAuthRepositoryImpl
    ): GoogleAuthRepository

    @Binds
    @Singleton
    abstract fun bindOrderSmsRepository(
        impl: OrderSmsRepositoryImpl
    ): OrderSmsRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAddressRepository(
        impl: AddressRepositoryImpl
    ): AddressRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindConsultRepository(
        impl: ConsultRepositoryImpl
    ): ConsultRepository

    @Binds
    @Singleton
    abstract fun bindAgoraRepository(
        impl: AgoraRepositoryImpl
    ): AgoraRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(
        impl: CurrencyRepositoryImpl
    ): CurrencyRepository

}