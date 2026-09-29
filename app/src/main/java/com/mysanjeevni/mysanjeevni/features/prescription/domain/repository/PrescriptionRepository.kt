package com.mysanjeevni.mysanjeevni.features.prescription.domain.repository

import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Prescription
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.PrescriptionModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
interface PrescriptionRepository {

    suspend fun uploadPrescription(
        file: MultipartBody.Part,
        productId: RequestBody,
        productName: RequestBody,
        userId: RequestBody
    ): Response<PrescriptionModel>

    suspend fun getPrescriptions(
        userId: String,
        consultationId: String? = null
    ): Result<List<Prescription>>
}