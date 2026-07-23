package com.mysanjeevni.mysanjeevni.features.cart.data.dto

data class CartItemDto(
    val productId:String,
    val name:String,
    val price: Double,
    val image: String,
    val qty: Int,
    val stock:Int,
    val requirePrescription: Boolean
)