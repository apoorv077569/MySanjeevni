package com.mysanjeevni.mysanjeevni.features.orders.data.dto


import com.google.gson.annotations.SerializedName

data class ServiceabilityRequestDto(

    @SerializedName("deliveryPincode")
    val deliveryPincode: String
)