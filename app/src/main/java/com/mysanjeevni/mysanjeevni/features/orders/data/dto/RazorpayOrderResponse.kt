package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class RazorpayOrderResponse(
    val success: Boolean,
    val order: RazorpayOrderDetailDto
)

data class RazorpayOrderDetailDto(

    @SerializedName("_id")
    val id: String,

    val userId: String,

    val items: List<RazorpayOrderItemDto>,

    val totalPrice: Double,

    val deliveryAddress: String,

    val status: String,

    val paymentStatus: String,

    val razorpayOrderId: String?,
    val shippingCharge: Double = 0.0

)

data class RazorpayOrderItemDto(
    val productId: String,
    val quantity: Int,
    val price: Double
)