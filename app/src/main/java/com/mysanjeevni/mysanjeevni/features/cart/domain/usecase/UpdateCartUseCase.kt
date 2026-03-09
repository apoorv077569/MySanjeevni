package com.mysanjeevni.mysanjeevni.features.cart.domain.usecase

import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val repository: CartRepository
){
    suspend operator fun invoke(itemId: Int,newQty:Int):List<CartItem>{
        return repository.updateQuantity(itemId,newQty)
    }
}