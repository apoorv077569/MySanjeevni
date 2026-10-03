package com.mysanjeevni.mysanjeevni.features.prescription.data.dto


import com.google.gson.annotations.SerializedName

data class PrescriptionResponseDto(

    @SerializedName("message")
    val message: String?,

    @SerializedName("prescriptions")
    val prescriptions: List<PrescriptionDto>?,

    @SerializedName("total")
    val total: Int?
)

data class PrescriptionDto(

    @SerializedName("_id")
    val id: String?,

    @SerializedName("doctorName")
    val doctorName: String?,

    @SerializedName("doctorRegistrationNumber")
    val doctorRegistrationNumber: String?,

    @SerializedName("doctorAddress")
    val doctorAddress: String?,

    @SerializedName("hospitalName")
    val hospitalName: String?,

    @SerializedName("issueDate")
    val issueDate: String?,

    @SerializedName("expiryDate")
    val expiryDate: String?,

    @SerializedName("medicines")
    val medicines: List<MedicineDto>?,

    @SerializedName("diagnosis")
    val diagnosis: String?,

    @SerializedName("notes")
    val notes: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("isVerified")
    val isVerified: Boolean?,

    @SerializedName("consultationId")
    val consultationId: ConsultationReferenceDto?
)

data class MedicineDto(

    @SerializedName("_id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("dosage")
    val dosage: String?,

    @SerializedName("frequency")
    val frequency: String?,

    @SerializedName("duration")
    val duration: String?
)

data class ConsultationReferenceDto(

    @SerializedName("_id")
    val id: String?,

    @SerializedName("doctorName")
    val doctorName: String?,

    @SerializedName("doctorDepartment")
    val doctorDepartment: String?,

    @SerializedName("doctorSpecialization")
    val doctorSpecialization: String?,

    @SerializedName("appointmentDate")
    val appointmentDate: String?,

    @SerializedName("allottedTime")
    val allottedTime: String?,

    @SerializedName("consultationType")
    val consultationType: String?,

    @SerializedName("symptoms")
    val symptoms: String?
)