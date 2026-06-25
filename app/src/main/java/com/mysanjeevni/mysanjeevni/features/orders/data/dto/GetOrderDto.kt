package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class GetOrderDto(

    @SerializedName("_id")
    val id: String,

    val userId: String,

    val items: List<GetOrderItemDto>,

    val totalPrice: Double,

    val deliveryAddress: GetOrderAddressDto,

    val status: String,

    val paymentStatus: String,

    val shippingCharge: Double,

    val createdAt: String
)