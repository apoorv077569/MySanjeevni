package com.mysanjeevni.mysanjeevni.features.cart.presentation.state

import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem

data class CartState(
    val isLoading: Boolean = false,
    val cartItem : List<CartItem> = emptyList(),
    val totalBill : Double = 0.0,
    val error : String = ""
)
