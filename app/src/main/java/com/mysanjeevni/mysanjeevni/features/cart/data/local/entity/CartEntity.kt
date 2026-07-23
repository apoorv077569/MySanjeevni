package com.mysanjeevni.mysanjeevni.features.cart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cart_items")
data class CartEntity(
    @PrimaryKey
    val productId: String,
    val userId:String,
    val name: String,
    val price: Double,
    val originalPrice: Double,
    val quantity:Int,
    val imageUrl: String,
    val stock:Int,
    val requirePrescription: Boolean
)