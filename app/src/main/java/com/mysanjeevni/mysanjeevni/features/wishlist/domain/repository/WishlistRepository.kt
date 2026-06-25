package com.mysanjeevni.mysanjeevni.features.wishlist.domain.repository

import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.AddWishlistRequest
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.WishlistItemDto
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.model.WishlistItem

interface WishlistRepository {

    suspend fun getWishlist(
        userId: String
    ): Result<List<WishlistItem>>

    suspend fun addToWishlist(
        request: AddWishlistRequest
    ): Result<WishlistItemDto>

    suspend fun removeFromWishlist(
        userId: String,
        productId: String
    ): Result<Unit>
}