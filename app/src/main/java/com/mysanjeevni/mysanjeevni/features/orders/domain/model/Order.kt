package com.mysanjeevni.mysanjeevni.features.orders.domain.model

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine

data class Order(
    val id: String,
    val userId: String,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val paymentStatus: String,
    val deliveryAddress: String,
    val status: String,
    val createdAt: String
)

data class OrderUiModel(
    val order: Order,
    val medicine: Medicine?
)