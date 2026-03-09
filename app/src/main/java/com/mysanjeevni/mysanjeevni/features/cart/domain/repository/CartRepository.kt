package com.mysanjeevni.mysanjeevni.features.cart.domain.repository

import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem

interface CartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun updateQuantity(item:Int,newQty: Int): List<CartItem>
    suspend fun addToCart(item: CartItem)
}