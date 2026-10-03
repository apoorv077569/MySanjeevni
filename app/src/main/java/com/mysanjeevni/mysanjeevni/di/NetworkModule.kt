package com.mysanjeevni.mysanjeevni.di

import com.mysanjeevni.mysanjeevni.features.payment.data.remote.PaymentApi
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrescriptionRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrescriptionOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AddressOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AddressRetrofit

