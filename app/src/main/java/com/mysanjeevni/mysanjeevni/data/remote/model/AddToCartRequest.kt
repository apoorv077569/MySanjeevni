package com.mysanjeevni.mysanjeevni.data.remote.model

data class AddToCartRequest(
    val userId:String,
    val productId:String,
    val name:String,
    val price:Double,
    val image:String
)
