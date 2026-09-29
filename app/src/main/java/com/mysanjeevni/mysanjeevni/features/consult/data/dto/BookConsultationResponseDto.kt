package com.mysanjeevni.mysanjeevni.features.consult.data.dto

import com.google.gson.annotations.SerializedName

data class BookConsultationResponseDto(

    @SerializedName("consultation")
    val consultation: ConsultationDto? = null,

    @SerializedName("message")
    val message: String? = null
)
