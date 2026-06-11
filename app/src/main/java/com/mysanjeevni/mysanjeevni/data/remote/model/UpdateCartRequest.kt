package com.mysanjeevni.mysanjeevni.data.remote.model

data class UpdateCartRequest(
    val userId: String,
    val productId: String,
    val qty:Int
)
