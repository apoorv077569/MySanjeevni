package com.mysanjeevni.mysanjeevni.features.labs.data.dto.cancel

import com.mysanjeevni.mysanjeevni.features.labs.data.dto.history.BookingHistoryItemDto

data class CancelBookingResponseDto(
    val message: String,
    val booking: BookingHistoryItemDto?,
    val refund: RefundDto?
)

data class RefundDto(
    val amount: Double? = 0.0,
    val status: String? = null
)