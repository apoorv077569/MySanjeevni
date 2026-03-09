package com.mysanjeevni.mysanjeevni.features.orders.domain.model

data class Order(
    val id:String,
    val date:String,
    val items:String,
    val totalAmount: Double,
    val status:String,
    val statusColor: Long
)
