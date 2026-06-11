package com.mysanjeevni.mysanjeevni.features.cart.domain.model

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val originalPrice:  Double,
    var qty: Int,
    val imageUrl: String,
)