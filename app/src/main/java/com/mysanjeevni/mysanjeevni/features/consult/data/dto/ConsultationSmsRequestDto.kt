package com.mysanjeevni.mysanjeevni.features.consult.data.dto

data class DoctorConsultationSmsRequestDto(
    val phone: String,
    val email: String,
    val patientName: String,
    val doctorId: String,
    val consultationType: String,
    val consultationDate: String,
    val consultationTime: String,
    val symptoms: String? = null,
    val medicalHistory: String? = null
)
