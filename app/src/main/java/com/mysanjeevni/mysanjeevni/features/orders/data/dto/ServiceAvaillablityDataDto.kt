package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class ServiceabilityDataDto(

    @SerializedName("serviceable")
    val serviceable: Boolean,

    @SerializedName("deliveryPincode")
    val deliveryPincode: String,

    @SerializedName("pickupPincode")
    val pickupPincode: String,

    @SerializedName("recommended")
    val recommended: RecommendedCourierDto,

    @SerializedName("codAvailable")
    val codAvailable: Boolean
)