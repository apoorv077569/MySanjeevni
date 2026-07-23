package com.mysanjeevni.mysanjeevni.features.payment.data.dto

data class RefundRequest(
    val paymentId: String,
    val reason: String,
    val amount: Double
)
data class RefundResponse(
    val success: Boolean,
    val refund: Refund
)
data class Refund(
    val id: String,
    val amount: Double,
    val currency: String,
    val status: String,
    val paymentId: String,
    val reason: String?,
    val createdAt: String
)