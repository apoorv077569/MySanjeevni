package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("userId") val userId: String?,
    @SerializedName("items") val items: List<OrderItemDto>,
    @SerializedName("totalPrice") val totalPrice: Double,
    @SerializedName("deliveryAddress") val deliveryAddressId: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("razorpayOrderId") val razorpayOrderId: String,
    @SerializedName("razorpayPaymentId") val razorpayPaymentId: String,
    @SerializedName("razorpaySignature") val razorpaySignature: String,
    val shippingCharge: Double?
)