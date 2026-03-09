package com.mysanjeevni.mysanjeevni.features.cart.domain.model

data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val originalPrice: Double,
    val qty: Int,
    val imageUrl: Int
)