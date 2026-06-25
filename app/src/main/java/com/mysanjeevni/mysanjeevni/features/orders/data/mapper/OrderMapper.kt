package com.mysanjeevni.mysanjeevni.features.orders.data.mapper

import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.OrderItem

fun OrderItemDto.toDomain() = OrderItem(
    productId = productId,
    name = name,
    quantity = quantity,
    price = price
)

fun OrderDto.toDomain() = Order(
    id = id,
    userId = userId,
    items = items.map { it.toDomain() },
    totalPrice = totalPrice,
    deliveryAddress = deliveryAddress,
    status = status,
    createdAt = createdAt,
    paymentStatus = paymentStatus
)