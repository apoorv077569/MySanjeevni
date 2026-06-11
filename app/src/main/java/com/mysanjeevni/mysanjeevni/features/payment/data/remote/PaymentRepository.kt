package com.mysanjeevni.mysanjeevni.features.payment.data.remote

interface PaymentRepository {
    suspend fun createRazorpayOrder(request: CreateRazorpayOrderRequest): Result<CreateRazorpayOrderResponse>
    suspend fun verifyPayment(request: VerifyPaymentRequest): Result<VerifyResponse>
}