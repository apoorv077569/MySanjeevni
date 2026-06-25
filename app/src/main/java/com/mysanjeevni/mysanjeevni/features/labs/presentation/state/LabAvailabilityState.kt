package com.mysanjeevni.mysanjeevni.features.labs.presentation.state

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.Serviceability
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsResult

data class LabAvailabilityState(
    val isCheckingServiceability: Boolean = false,
    val serviceability: Serviceability? = null,
    val isServiceable: Boolean? = null,   // null = not checked yet
    val serviceabilityMessage: String? = null,
    val serviceabilityError: String? = null,

    val isLoadingSlots: Boolean = false,
    val slotsResult: SlotsResult? = null,
    val slotsError: String? = null,

    val selectedSlotId: String? = null
)