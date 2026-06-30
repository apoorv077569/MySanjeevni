package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("_id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("items") val items: List<OrderItemDto>,
    @SerializedName("totalPrice") val totalPrice: Double,
    @SerializedName("deliveryAddress") val deliveryAddress: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("paymentStatus") val paymentStatus: String
)
