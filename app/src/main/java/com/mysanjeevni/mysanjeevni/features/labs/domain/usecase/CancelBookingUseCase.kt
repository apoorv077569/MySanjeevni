package com.mysanjeevni.mysanjeevni.features.labs.domain.usecase

import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.BookingHistoryRepository
import javax.inject.Inject

class CancelBookingUseCase @Inject constructor(
    private val repository: BookingHistoryRepository
) {

    suspend operator fun invoke(
        bookingId: String
    ) = repository.cancelBooking(
        bookingId
    )
}