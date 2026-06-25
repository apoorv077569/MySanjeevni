package com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot

data class SlotsResponseDto(
    val provider: String,
    val timeZone: String,
    val appointmentDate: String,
    val slots: List<SlotDto>
)

data class SlotDto(
    val id: String,
    val startTime: String,
    val endTime: String,
    val label: String
)