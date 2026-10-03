package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class ServiceabilityResponseDto(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: ServiceabilityDataDto
)