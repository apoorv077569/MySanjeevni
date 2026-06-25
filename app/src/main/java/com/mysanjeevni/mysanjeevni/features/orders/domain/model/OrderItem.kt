package com.mysanjeevni.mysanjeevni.features.orders.domain.model

data class OrderItem(
    val productId: String,
    val name: String? = null,

    val quantity: Int,
    val price: Double
)