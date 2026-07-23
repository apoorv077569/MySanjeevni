package com.mysanjeevni.mysanjeevni.features.prescription.domain.usecase

import android.util.Log
import com.mysanjeevni.mysanjeevni.features.prescription.data.model.PrescriptionModel
import com.mysanjeevni.mysanjeevni.features.prescription.domain.repository.PrescriptionRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class UploadPrescriptionUseCase @Inject constructor(
    private val repository: PrescriptionRepository
) {

    companion object {
        private const val TAG = "PRESCRIPTION_USECASE"
    }

    suspend operator fun invoke(
        file: MultipartBody.Part,
        productId: RequestBody,
        productName: RequestBody,
        userId: RequestBody
    ): Response<PrescriptionModel> {

        Log.d(TAG, "========================================")
        Log.d(TAG, "UploadPrescriptionUseCase invoked")
        Log.d(TAG, "File type: ${file.body.contentType()}")
        Log.d(TAG, "File size: ${file.body.contentLength()} bytes")
        Log.d(TAG, "========================================")

        return try {

            val response = repository.uploadPrescription(
                file = file,
                productId = productId,
                productName = productName,
                userId = userId
            )

            Log.d(TAG, "Repository response received")
            Log.d(TAG, "HTTP Code: ${response.code()}")
            Log.d(TAG, "Success: ${response.isSuccessful}")

            response

        } catch (e: Exception) {

            Log.e(TAG, "Prescription upload failed")
            Log.e(TAG, "Exception: ${e.javaClass.simpleName}")
            Log.e(TAG, "Message: ${e.message}", e)

            throw e
        }
    }
}