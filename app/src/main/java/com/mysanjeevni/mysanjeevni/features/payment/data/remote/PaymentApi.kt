package com.mysanjeevni.mysanjeevni.features.payment.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {

    @POST("api/payments/razorpay/create-order")
    suspend fun createRazorpayOrder(
        @Body request: CreateRazorpayOrderRequest
    ): Response<CreateRazorpayOrderResponse>

    @POST("api/payments/razorpay/verify-order")
    suspend fun verifyPayment(
        @Body request: VerifyPaymentRequest
    ): Response<VerifyResponse>
}