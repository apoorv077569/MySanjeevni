package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class CancelOrderResponse(

    val message: String,

    val order: CancelledOrderDto
)

data class CancelledOrderDto(

    @SerializedName("_id")
    val id: String,

    val status: String,

    val paymentStatus: String
)