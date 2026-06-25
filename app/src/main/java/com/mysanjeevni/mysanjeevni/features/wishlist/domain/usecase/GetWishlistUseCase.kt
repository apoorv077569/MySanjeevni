package com.mysanjeevni.mysanjeevni.features.wishlist.domain.usecase

import com.mysanjeevni.mysanjeevni.features.wishlist.domain.model.WishlistItem
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.repository.WishlistRepository
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {

    suspend operator fun invoke(
        userId: String
    ): Result<List<WishlistItem>> {

        return repository.getWishlist(
            userId
        )
    }
}