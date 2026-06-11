package com.mysanjeevni.mysanjeevni.features.cart.domain.model

data class CartItemDto(
    val productId:String,
    val name:String,
    val price: Double,
    val image: String,
    val qty: Int,
)
