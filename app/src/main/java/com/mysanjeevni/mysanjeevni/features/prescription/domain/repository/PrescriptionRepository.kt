package com.mysanjeevni.mysanjeevni.features.prescription.domain.repository

import android.net.Uri

interface PrescriptionRepository {

    suspend fun uploadPrescription(
        imageUri: Uri
    ): Result<String>
}