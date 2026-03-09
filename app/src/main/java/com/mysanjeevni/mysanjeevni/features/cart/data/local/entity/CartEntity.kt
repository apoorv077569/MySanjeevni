package com.mysanjeevni.mysanjeevni.features.cart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    val name: String,
    val price: Double,
    val originalPrice: Double,
    val qty:Int,
    val imageRes: Int
)
