package com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot

data class SlotsRequestDto(
    val testId: String,
    val testName: String,
    val appointmentDate: String,
    val pincode: String,
    val patientName: String? = null,
    val patientAge: Int? = null,
    val patientGender: String? = null
)