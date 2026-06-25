package com.mysanjeevni.mysanjeevni.features.wishlist.domain.usecase

import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.AddWishlistRequest
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.WishlistItemDto
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.repository.WishlistRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {

    suspend operator fun invoke(
        request: AddWishlistRequest
    ): Result<WishlistItemDto> {

        return repository.addToWishlist(
            request
        )
    }
}