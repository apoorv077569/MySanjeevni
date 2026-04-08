package com.mysanjeevni.mysanjeevni.features.cart.domain.repository

import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
     fun getCartItems(): Flow<List<CartItem>>
    suspend fun updateQuantity(item:Int,newQty: Int): List<CartItem>
    suspend fun addToCart(item: CartItem)
}