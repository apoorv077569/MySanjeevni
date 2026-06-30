package com.mysanjeevni.mysanjeevni.features.orders.data.mapper

import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.OrderItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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

    createdAt = createdAt ?: SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.US
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date()),
    paymentStatus = paymentStatus
)