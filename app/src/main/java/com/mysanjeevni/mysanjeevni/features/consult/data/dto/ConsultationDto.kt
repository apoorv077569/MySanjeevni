package com.mysanjeevni.mysanjeevni.features.consult.data.dto


import com.google.gson.annotations.SerializedName

data class ConsultationDto(

    @SerializedName("_id")
    val id: String?,

    val userId: String?,
    val doctorId: String?,

    val patientName: String?,
    val patientPhone: String?,
    val patientEmail: String?,

    val doctorName: String?,
    val doctorDepartment: String?,
    val doctorSpecialization: String?,

    val appointmentDate: String?,
    val preferredTimeSlot: String?,
    val allottedTime: String?,

    val consultationType: String?,

    val queueNumber: Int?,
    val patientsAhead: Int?,

    val status: String?,

    val fees: Double?,
    val paymentStatus: String?,
    val paymentMethod: String?,

    val symptoms: String?,
    val notes: String?,
    val prescription: String?,
    val feedback: String?
)

data class ConsultationResponseDto(
    val consultations: List<ConsultationDto>?
)