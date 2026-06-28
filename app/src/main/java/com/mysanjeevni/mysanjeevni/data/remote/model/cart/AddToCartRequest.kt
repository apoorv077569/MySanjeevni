package com.mysanjeevni.mysanjeevni.data.remote.model.cart

data class AddToCartRequest(
    val userId:String,
    val productId:String,
    val name:String,
    val price:Double,
    val image:String
)