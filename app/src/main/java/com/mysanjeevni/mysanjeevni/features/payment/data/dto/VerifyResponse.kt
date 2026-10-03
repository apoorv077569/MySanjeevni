package com.mysanjeevni.mysanjeevni.features.payment.data.dto

import com.google.gson.annotations.SerializedName

data class VerifyResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)