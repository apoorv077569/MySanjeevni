package com.mysanjeevni.mysanjeevni.features.consult.domnain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Consultation(
    val id: String,
    val userId: String,
    val doctorId: String,

    val patientName: String,
    val patientPhone: String,
    val patientEmail: String,

    val doctorName: String,
    val specialization: String,
    val department: String,

    val appointmentDate: String,
    val allottedTime: String,

    val consultationType: String,

    val queueNumber: Int,
    val patientsAhead: Int,

    val status: String,

    val fees: Double,
    val paymentStatus: String,

    val symptoms: String,
    val notes: String,

    val prescription: String?,
    val feedback: String
) : Parcelable