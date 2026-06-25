package com.mysanjeevni.mysanjeevni.features.labs.presentation.state

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.BookingHistory

data class BookingHistoryState(
    val isLoading: Boolean = false,
    val bookings: List<BookingHistory> = emptyList(),
    val error: String? = null,
    val isCancelling:Boolean = false,
    val cancelSuccess: Boolean = false,
    val isSyncing: Boolean = false
)