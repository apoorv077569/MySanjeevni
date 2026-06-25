package com.mysanjeevni.mysanjeevni.features.wishlist.data.dto

data class GetWishlistResponse(
    val message: String,
    val items: List<WishlistItemDto>,
    val total: Int
)