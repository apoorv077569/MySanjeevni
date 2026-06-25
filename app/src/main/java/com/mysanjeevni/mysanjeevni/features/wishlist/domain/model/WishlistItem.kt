package com.mysanjeevni.mysanjeevni.features.wishlist.domain.model

data class WishlistItem(
    val id: String,
    val productId: String,
    val productName: String,
    val price: Double,
    val image: String
)