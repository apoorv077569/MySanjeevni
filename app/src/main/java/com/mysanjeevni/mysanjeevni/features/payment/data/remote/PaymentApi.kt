package com.mysanjeevni.mysanjeevni.features.payment.data.remote

import com.mysanjeevni.mysanjeevni.features.payment.data.dto.CreateRazorpayOrderRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.CreateRazorpayOrderResponse
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.VerifyPaymentRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.VerifyResponse
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