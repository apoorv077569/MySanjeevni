package com.mysanjeevni.mysanjeevni.features.labs.data.mapper

import com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot.ServiceabilityResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot.SlotDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot.SlotsResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.Serviceability
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.Slot
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsResult

fun ServiceabilityResponseDto.toDomain(): Serviceability {
    return Serviceability(
        provider = provider,
        pincode = pincode,
        isServiceable = isServiceable,
        serviceTypes = serviceTypes
    )
}

fun SlotDto.toDomain(): Slot {
    return Slot(
        id = id,
        startTime = startTime,
        endTime = endTime,
        label = label
    )
}

fun SlotsResponseDto.toDomain(): SlotsResult {
    return SlotsResult(
        provider = provider,
        timeZone = timeZone,
        appointmentDate = appointmentDate,
        slots = slots.map { it.toDomain() }
    )
}