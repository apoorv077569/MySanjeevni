package com.mysanjeevni.mysanjeevni.features.orders.data.dto

data class GetOrderItemDto(
    val productId: String,
    val quantity: Int,
    val price: Double
)