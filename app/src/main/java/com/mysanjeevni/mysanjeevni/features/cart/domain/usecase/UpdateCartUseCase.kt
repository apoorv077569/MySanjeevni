package com.mysanjeevni.mysanjeevni.features.cart.domain.usecase

import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val repository: CartRepository
){
    suspend operator fun invoke(
        productId: String,
        userId: String,
        newQty: Int
    ) {
        repository.updateQuantity(productId, userId, newQty)
    }
}