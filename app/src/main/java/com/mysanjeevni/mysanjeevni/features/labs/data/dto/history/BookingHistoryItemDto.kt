package com.mysanjeevni.mysanjeevni.features.labs.data.dto.history

import com.google.gson.annotations.SerializedName

data class BookingHistoryItemDto(

    @SerializedName("_id")
    val id: String,

    val testId: String? = null,
    val testName: String? = null,

    val status: String? = null,

    val provider: String? = null,
    val providerStatus: String? = null,

    val collectionDate: String? = null,
    val createdAt: String? = null,

    @SerializedName("testPrice")
    val amount: Double? = null,

    val userId: String? = null,
    val patientName: String?,
    val patientPhone: String?,
    val address: String?
)