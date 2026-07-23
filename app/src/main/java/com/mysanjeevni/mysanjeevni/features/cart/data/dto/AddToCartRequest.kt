package com.mysanjeevni.mysanjeevni.features.cart.data.dto

data class AddToCartRequest(
    val userId:String,
    val productId:String,
    val name:String,
    val price:Double,
    val image:String
)
