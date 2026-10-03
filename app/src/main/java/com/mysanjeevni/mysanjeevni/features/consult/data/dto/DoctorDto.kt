package com.mysanjeevni.mysanjeevni.features.consult.data.dto


import com.google.gson.annotations.SerializedName

data class DoctorDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("experience")
    val experience: Double?,


    @SerializedName("specialization")
    val specialization: String?,

    @SerializedName("department")
    val department: String?,

    @SerializedName("rating")
    val rating: Double?,

    @SerializedName("consultationFee")
    val consultationFee: Double,

    @SerializedName("isAvailable")
    val isAvailable: Boolean,

    @SerializedName("totalReviews")
    val totalReviews: Int = 0,

    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("availableDates")
    val availableDates: List<String?> = emptyList(),

    @SerializedName("timeSlots")
    val timeSlots: List<String?> = emptyList()
)