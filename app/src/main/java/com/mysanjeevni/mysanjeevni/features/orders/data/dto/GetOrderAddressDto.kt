package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class GetOrderAddressDto(
    @SerializedName("_id")
    val id:String,
    val fullName: String,
    val phone: String,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val state: String,
    val pincode: String
)
