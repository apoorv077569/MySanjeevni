package com.mysanjeevni.mysanjeevni.features.labs.domain.model

data class SlotsResult(
    val provider: String,
    val timeZone: String,
    val appointmentDate: String,
    val slots: List<Slot>
)

data class Slot(
    val id: String,
    val startTime: String,
    val endTime: String,
    val label: String
)

data class SlotsRequestParams(
    val testId: String,
    val testName: String,
    val appointmentDate: String,
    val pincode: String,
    val patientName: String? = null,
    val patientAge: Int? = null,
    val patientGender: String? = null
)