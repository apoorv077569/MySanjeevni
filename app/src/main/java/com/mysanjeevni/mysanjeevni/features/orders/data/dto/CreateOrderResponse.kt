package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(
    @SerializedName("message") val message: String,
    @SerializedName("order") val order: OrderDto,
    @SerializedName("razorpayOrder") val razorpayOrder: RazorpayOrderDto
)

data class RazorpayOrderDto(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("receipt") val receipt: String
)