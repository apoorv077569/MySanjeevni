package com.mysanjeevni.mysanjeevni.features.consult.data.dto


import com.google.gson.annotations.SerializedName

data class RefundDto(

    @SerializedName("id")
    val id: String?,

    @SerializedName("status")
    val status: String?
)

data class CancelConsultationResponseDto(

    @SerializedName("consultation")
    val consultation: ConsultationDto?,

    @SerializedName("refund")
    val refund: RefundDto?,

    @SerializedName("message")
    val message: String?
)
