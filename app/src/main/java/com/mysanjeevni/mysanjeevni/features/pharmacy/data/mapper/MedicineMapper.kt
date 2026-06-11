package com.mysanjeevni.mysanjeevni.features.pharmacy.data.mapper


import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem

fun Medicine.toCartItem(): CartItem {
    return CartItem(
        id = this.id,
        name = this.name,
        price = this.price.toDouble(),
        originalPrice = this.price.toDouble(),
        qty = 1,
        imageUrl = this.image,
    )
}