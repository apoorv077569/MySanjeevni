package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class RecommendedCourierDto(

    @SerializedName("courierCompanyId")
    val courierCompanyId: Int,

    @SerializedName("courierName")
    val courierName: String?,

    @SerializedName("rate")
    val rate: Double,

    @SerializedName("estimatedDeliveryDate")
    val estimatedDeliveryDate: String,

    @SerializedName("estimatedDeliveryDays")
    val estimatedDeliveryDays: String,

    @SerializedName("codAvailable")
    val codAvailable: Boolean,

    @SerializedName("rating")
    val rating: Double
)