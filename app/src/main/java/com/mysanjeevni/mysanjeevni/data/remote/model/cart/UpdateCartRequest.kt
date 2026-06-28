package com.mysanjeevni.mysanjeevni.data.remote.model.cart

import com.google.gson.annotations.SerializedName

data class UpdateCartRequest(
    @SerializedName("userId")
    val userId: String,
    val productId: String,
    val qty:Int
)