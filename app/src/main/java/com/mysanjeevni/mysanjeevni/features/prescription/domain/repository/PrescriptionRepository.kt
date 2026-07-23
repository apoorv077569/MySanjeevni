package com.mysanjeevni.mysanjeevni.features.prescription.domain.repository

import android.net.Uri
import com.mysanjeevni.mysanjeevni.features.prescription.data.model.PrescriptionModel
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
}