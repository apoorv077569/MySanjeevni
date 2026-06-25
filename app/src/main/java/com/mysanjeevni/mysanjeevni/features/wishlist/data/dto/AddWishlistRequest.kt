package com.mysanjeevni.mysanjeevni.features.wishlist.data.dto

data class AddWishlistRequest(
    val userId: String,
    val productId: String,
    val productName: String,
    val price: Double,
    val image: String
)