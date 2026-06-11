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

