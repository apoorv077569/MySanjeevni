package com.mysanjeevni.mysanjeevni.features.labs.data.dto

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabBooking

data class LabBookingResponseDto(
    val message: String,
    val booking: LabBooking
)
