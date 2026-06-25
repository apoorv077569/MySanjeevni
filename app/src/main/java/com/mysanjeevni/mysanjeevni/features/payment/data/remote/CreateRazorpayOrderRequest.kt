package com.mysanjeevni.mysanjeevni.features.payment.data.remote

import com.google.gson.annotations.SerializedName

data class CreateRazorpayOrderRequest(
    @SerializedName("amount") val amount: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("receipt") val receipt: String,
    @SerializedName("notes") val notes: Map<String, String>? = null
)