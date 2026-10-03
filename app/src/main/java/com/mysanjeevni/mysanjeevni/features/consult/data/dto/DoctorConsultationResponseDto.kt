package com.mysanjeevni.mysanjeevni.features.consult.data.dto


data class DoctorConsultationSmsResponseDto(
    val success: Boolean,
    val consultationId: String?,
    val consultationDateTime: String?,
    val consultationType: String?,
    val patientName: String?,
    val message: String?
)