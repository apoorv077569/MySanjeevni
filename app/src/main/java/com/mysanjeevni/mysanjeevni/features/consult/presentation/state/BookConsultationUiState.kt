package com.mysanjeevni.mysanjeevni.features.consult.presentation.state

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor

data class BookConsultationUiState(
    val doctor: Doctor?=null,
    val patientName: String = "",
    val phone:String = "",
    val email: String = "",
    val appointmentDate: String = "",
    val consultationType: String = "In-Person-Visit",
    val symptoms: String = "",
    val isLoading: Boolean = false,
    val error: String?=null,
    val isBookingSuccessful: Boolean = false
)
