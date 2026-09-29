package com.mysanjeevni.mysanjeevni.features.prescription.domain.model

data class PrescriptionModel(
    val success: Boolean,
    val message: String?,
    val prescriptionUrl: String?,
    val publicId: String?,
    val productId: String?,
    val productName: String?
)