package com.mysanjeevni.mysanjeevni.features.consult.data.dto

import com.google.gson.annotations.SerializedName

data class BookConsultationRequestDto(

    @SerializedName("userId")
    val userId: String?,

    @SerializedName("doctorId")
    val doctorId: String,

    @SerializedName("patientName")
    val patientName: String,

    @SerializedName("patientPhone")
    val patientPhone: String? = null,

    @SerializedName("patientEmail")
    val patientEmail: String? = null,

    @SerializedName("appointmentDate")
    val appointmentDate: String,

    @SerializedName("consultationType")
    val consultationType: String? = null,

    @SerializedName("symptoms")
    val symptoms: String? = null,

    @SerializedName("razorpayOrderId")
    val razorpayOrderId: String? = null,

    @SerializedName("razorpayPaymentId")
    val razorpayPaymentId: String? = null,

    @SerializedName("razorpaySignature")
    val razorpaySignature: String? = null
)