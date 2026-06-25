package com.mysanjeevni.mysanjeevni.features.labs.domain.model



import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingHistory(
    val id: String,
    val testName: String,
    val status: String,
    val amount: Double,
    val collectionDate: String,
    val createdAt: String,

    val patientName: String?,
    val patientPhone: String?,
    val address: String?
) : Parcelable