package com.mysanjeevni.mysanjeevni.features.labs.domain.repository

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.BookingHistory

interface BookingHistoryRepository {
    suspend fun getBookingHistory(): Result<List<BookingHistory>>
    suspend fun cancelBooking(
        bookingId: String
    ): Result<String>
    suspend fun syncBooking(
        bookingId: String
    ): Result<BookingHistory>
}