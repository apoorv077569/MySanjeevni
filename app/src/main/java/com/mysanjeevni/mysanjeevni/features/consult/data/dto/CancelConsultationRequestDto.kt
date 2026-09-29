package com.mysanjeevni.mysanjeevni.features.consult.data.dto

import com.google.gson.annotations.SerializedName

data class CancelConsultationRequestDto(
    @SerializedName("status")
    val status: String = "cancelled"
)
