package com.mysanjeevni.mysanjeevni.features.labs.data.mapper


import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabBookingDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabBooking

fun LabBookingDto.toDomain(): LabBooking {
    return LabBooking(
    _id = _id,
        status = status,
        testName = testName,
        testPrice = testPrice
    )
}