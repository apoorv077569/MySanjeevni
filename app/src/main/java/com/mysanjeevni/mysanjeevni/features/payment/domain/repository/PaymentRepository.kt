package com.mysanjeevni.mysanjeevni.features.payment.domain.repository

import com.mysanjeevni.mysanjeevni.features.payment.data.dto.CreateRazorpayOrderRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.CreateRazorpayOrderResponse
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.VerifyPaymentRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.VerifyResponse

interface PaymentRepository {
    suspend fun createRazorpayOrder(request: CreateRazorpayOrderRequest): Result<CreateRazorpayOrderResponse>
    suspend fun verifyPayment(request: VerifyPaymentRequest): Result<VerifyResponse>
}