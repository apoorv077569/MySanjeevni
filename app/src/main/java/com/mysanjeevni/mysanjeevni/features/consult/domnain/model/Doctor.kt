package com.mysanjeevni.mysanjeevni.features.consult.domnain.model

data class Doctor(
    val id:String,
    val name:String,
    val specialization:String,
    val department:String,
    val rating:Double,
    val experience: Double,
    val consultationFee: Double,
    val isAvailable: Boolean,
    val totalReviews: Int,
    val avatar: String,
    val availableDates: List<String?>,
    val timeSlots: List<String?>
)