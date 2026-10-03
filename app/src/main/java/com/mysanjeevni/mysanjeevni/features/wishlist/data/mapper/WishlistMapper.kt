package com.mysanjeevni.mysanjeevni.features.wishlist.data.mapper

import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.WishlistItemDto
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.model.WishlistItem

fun WishlistItemDto.toDomain(): WishlistItem {
    return WishlistItem(
        id = id,
        productId = productId,
        productName = productName,
        price = price,
        image = image,
        stock = stock,
        requirePrescription = requirePrescription
    )
}