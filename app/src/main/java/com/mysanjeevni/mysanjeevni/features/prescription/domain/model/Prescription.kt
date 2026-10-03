package com.mysanjeevni.mysanjeevni.features.prescription.domain.model


data class Prescription(
    val id: String,
    val doctorName: String,
    val doctorRegistrationNumber: String,
    val doctorAddress: String,
    val hospitalName: String?,
    val issueDate: String,
    val expiryDate: String,
    val medicines: List<Medicine>,
    val diagnosis: String,
    val notes: String,
    val status: String,
    val isVerified: Boolean,

    // Consultation information useful for UI
    val consultationId: String?,
    val appointmentDate: String?,
    val allottedTime: String?,
    val consultationType: String?,
    val symptoms: String?
)

data class Medicine(
    val id: String,
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String
)