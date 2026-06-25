package com.mysanjeevni.mysanjeevni.features.labs.presentation.state

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabBooking


data class LabBookingState(
    val isLoading: Boolean = false,
    val booking: LabBooking? = null,
    val successMessage: String? = null,
    val error: String? = null
)