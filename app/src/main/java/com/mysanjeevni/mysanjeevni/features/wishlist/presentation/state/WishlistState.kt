package com.mysanjeevni.mysanjeevni.features.wishlist.presentation.state

import com.mysanjeevni.mysanjeevni.features.wishlist.domain.model.WishlistItem

data class WishlistState(
    val isLoading: Boolean = false,
    val items: List<WishlistItem> = emptyList(),
    val error: String? = null
)