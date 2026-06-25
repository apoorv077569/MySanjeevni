package com.mysanjeevni.mysanjeevni.features.orders.data.mapper

import com.mysanjeevni.mysanjeevni.features.orders.data.dto.GetOrderDto
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.OrderItem

fun GetOrderDto.toDomain(): Order {

    return Order(
        id = id,
        userId = userId,

        items = items.map {
            OrderItem(
                productId = it.productId,
                quantity = it.quantity,
                price = it.price
            )
        },

        totalPrice = totalPrice,

        deliveryAddress =
            "${deliveryAddress.fullName}, ${deliveryAddress.city}",

        status = status,

        paymentStatus = paymentStatus,

        createdAt = createdAt
    )
}