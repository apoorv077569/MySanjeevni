package com.mysanjeevni.mysanjeevni.features.orders.domain.model

data class Order(
    val id: String,
    val userId: String,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val deliveryAddress: String,
    val status: String,
    val createdAt: String
)