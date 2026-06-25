package com.mysanjeevni.mysanjeevni.features.labs.domain.model

data class LabBooking(
    val _id: String,
    val status: String,
    val testName: String,
    val testPrice: Double
)