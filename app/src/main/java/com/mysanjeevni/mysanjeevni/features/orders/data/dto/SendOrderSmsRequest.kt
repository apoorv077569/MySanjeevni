package com.mysanjeevni.mysanjeevni.features.orders.data.dto

data class SendOrderSmsRequest(
    val phone: String,
    val email: String,
    val items: List<SmsOrderItemDto>,
    val shippingAddress: SmsShippingAddressDto
)

data class SmsOrderItemDto(
    val productId: String,
    val quantity: Int,
    val price: Double
)

data class SmsShippingAddressDto(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String
)
