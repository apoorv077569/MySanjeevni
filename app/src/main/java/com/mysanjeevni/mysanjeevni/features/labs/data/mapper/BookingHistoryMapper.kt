package com.mysanjeevni.mysanjeevni.features.labs.data.mapper

import com.mysanjeevni.mysanjeevni.features.labs.data.dto.history.BookingHistoryItemDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.BookingHistory

fun BookingHistoryItemDto.toDomain() =
    BookingHistory(
        id = id,
        testName = testName.orEmpty(),
        status = status.orEmpty(),
        amount = amount ?: 0.0,
        collectionDate = collectionDate.orEmpty(),
        createdAt = createdAt.orEmpty(),
        patientName = patientName.orEmpty(),
        patientPhone = patientPhone.orEmpty(),
        address = address.orEmpty()
    )