package com.mysanjeevni.mysanjeevni.features.orders.domain.model

data class Serviceability(

    val serviceable: Boolean,

    val courierName: String,

    val deliveryCharge: Double,

    val estimatedDeliveryDate: String,

    val estimatedDeliveryDays: String,

    val codAvailable: Boolean
)