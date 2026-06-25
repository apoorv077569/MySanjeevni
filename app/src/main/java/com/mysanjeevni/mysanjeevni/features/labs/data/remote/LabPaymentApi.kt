package com.mysanjeevni.mysanjeevni.features.labs.data.remote

import com.mysanjeevni.mysanjeevni.features.payment.data.remote.CreateRazorpayOrderRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.CreateRazorpayOrderResponse
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.VerifyPaymentRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.VerifyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LabPaymentApi {

    @POST("api/payments/razorpay/create-order")
    suspend fun createRazorpayOrder(
        @Body request: CreateRazorpayOrderRequest
    ): Response<CreateRazorpayOrderResponse>

    @POST("api/payments/razorpay/verify-order")
    suspend fun verifyPayment(
        @Body request: VerifyPaymentRequest
    ): Response<VerifyResponse>
}