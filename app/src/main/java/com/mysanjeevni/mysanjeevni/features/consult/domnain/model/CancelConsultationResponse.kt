package com.mysanjeevni.mysanjeevni.features.consult.domnain.model


data class CancelConsultationResponse(
    val consultation: Consultation?,
    val refundId: String?,
    val refundStatus: String?,
    val message: String
)
