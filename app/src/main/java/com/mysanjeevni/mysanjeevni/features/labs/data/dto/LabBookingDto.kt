package com.mysanjeevni.mysanjeevni.features.labs.data.dto

data class LabBookingDto(
    val _id: String,
    val testId: String,
    val testName: String,
    val testPrice: Double,
    val status: String
)