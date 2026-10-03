package com.mysanjeevni.mysanjeevni.features.wishlist.data.dto

import com.google.gson.annotations.SerializedName

data class WishlistItemDto(
    @SerializedName("_id")
    val id: String,

    val productId:String,
    val productName: String,
    val price: Double,
    val image: String,
    val stock:Int,
    val requirePrescription: Boolean
)
