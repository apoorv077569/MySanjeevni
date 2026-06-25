package com.mysanjeevni.mysanjeevni.features.labs.data.dto.history

import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabBookingDto

data class BookingHistoryResponseDto(
    val bookings: List<BookingHistoryItemDto>,
    val total: Int,
    val roleScope: String
)

data class SyncBookingResponseDto(
    val message: String,
    val booking: BookingHistoryItemDto?
)