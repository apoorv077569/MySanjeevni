package com.mysanjeevni.mysanjeevni.features.cart.domain.usecase

import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
){
     operator fun invoke(): Flow<List<CartItem>>{
        return repository.getCartItems()
    }
}