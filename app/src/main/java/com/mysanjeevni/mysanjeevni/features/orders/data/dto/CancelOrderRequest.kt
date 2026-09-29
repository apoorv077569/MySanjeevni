package com.mysanjeevni.mysanjeevni.features.orders.data.dto

data class CancelOrderRequest(
    val orderId: String,
    val status: String = "cancelled",
    val userId: String
)
