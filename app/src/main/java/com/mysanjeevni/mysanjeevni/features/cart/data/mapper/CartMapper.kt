package com.mysanjeevni.mysanjeevni.features.cart.data.mapper

import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItemDto

fun CartItemDto.toDomain(): CartItem {
    return CartItem(
        productId,
        name,
        price,
        price,
        qty,
        image,
    )
}