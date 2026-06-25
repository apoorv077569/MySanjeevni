package com.mysanjeevni.mysanjeevni.features.cart.domain.usecase

import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val repository: CartRepository
) {

    suspend operator fun invoke(
        productId: String,
        userId: String
    ) {
        repository.removeLocal(
            productId = productId,
            userId = userId
        )
    }
}