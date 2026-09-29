package com.mysanjeevni.mysanjeevni.features.consult.domnain.model

data class BookConsultationRequest(

    val userId: String?,

    val doctorId: String,

    val patientName: String,

    val patientPhone: String? = null,

    val patientEmail: String? = null,

    val appointmentDate: String,

    val consultationType: String? = null,

    val symptoms: String? = null,

    val razorpayOrderId: String? = null,

    val razorpayPaymentId: String? = null,

    val razorpaySignature: String? = null
)
