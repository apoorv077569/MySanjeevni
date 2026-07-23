package com.mysanjeevni.mysanjeevni.features.cart.data.dto

import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartData

data class CartResponse(
    val success: Boolean,
    val cart: CartData
)