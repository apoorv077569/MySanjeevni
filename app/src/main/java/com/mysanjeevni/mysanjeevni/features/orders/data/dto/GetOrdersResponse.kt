package com.mysanjeevni.mysanjeevni.features.orders.data.dto

import com.google.gson.annotations.SerializedName

data class GetOrdersResponse(
    @SerializedName("message") val message: String,
    @SerializedName("orders") val orders: List<GetOrderDto>,
    @SerializedName("total") val total: Int
)