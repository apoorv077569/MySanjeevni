package com.mysanjeevni.mysanjeevni.features.cart.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateCartRequest(
    @SerializedName("userId")
    val userId: String,
    val productId: String,
    val qty:Int
)