package com.mysanjeevni.mysanjeevni.features.payment.data.dto

import com.google.gson.annotations.SerializedName

data class VerifyPaymentRequest(
    @SerializedName("razorpay_order_id") val razorpayOrderId: String,
    @SerializedName("razorpay_payment_id") val razorpayPaymentId: String,
    @SerializedName("razorpay_signature") val razorpaySignature: String
)