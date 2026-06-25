package com.mysanjeevni.mysanjeevni.features.wishlist.domain.usecase

import com.mysanjeevni.mysanjeevni.features.wishlist.domain.repository.WishlistRepository
import javax.inject.Inject

class RemoveWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {

    suspend operator fun invoke(
        userId: String,
        productId: String
    ): Result<Unit> {

        return repository.removeFromWishlist(
            userId,
            productId
        )
    }
}