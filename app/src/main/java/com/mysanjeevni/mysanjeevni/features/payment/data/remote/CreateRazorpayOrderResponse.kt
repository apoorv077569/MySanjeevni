package com.mysanjeevni.mysanjeevni.features.payment.data.remote

import com.google.gson.annotations.SerializedName

data class CreateRazorpayOrderResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("order") val order: RazorpayOrder?
)

data class RazorpayOrder(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("receipt") val receipt: String
)