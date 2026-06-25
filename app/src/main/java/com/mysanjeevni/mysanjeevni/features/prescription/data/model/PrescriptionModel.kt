package com.mysanjeevni.mysanjeevni.features.prescription.data.model

data class PrescriptionModel(
    val id: String = "",
    val imageUri: String = "",
    val uploadedAt: Long = System.currentTimeMillis()
)