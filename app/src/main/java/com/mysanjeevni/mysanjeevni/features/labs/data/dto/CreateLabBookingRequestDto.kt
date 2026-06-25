package com.mysanjeevni.mysanjeevni.features.labs.data.dto

data class CreateLabBookingRequestDto(
    val testId: String,
    val testName: String,
    val testPrice: Double,

    val collectionType: String,

    val collectionDate: String,
    val collectionTime: String,

    val address: String,

    val notes: String?,

    val razorpayOrderId: String,
    val razorpayPaymentId: String,
    val razorpaySignature: String,

    val patientPincode: String,
    val patientAge: Int,
    val patientGender: String
)