package com.mysanjeevni.mysanjeevni.features.prescription.data.repository

import com.mysanjeevni.mysanjeevni.features.prescription.domain.repository.PrescriptionRepository
import android.util.Log
import com.mysanjeevni.mysanjeevni.features.prescription.data.model.PrescriptionModel
import com.mysanjeevni.mysanjeevni.features.prescription.data.remote.PrescriptionApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class PrescriptionRepositoryImpl  @Inject constructor(
    private val apiService: PrescriptionApiService
) : PrescriptionRepository {

    companion object {
        private const val TAG = "PRESCRIPTION_REPO"
    }
    override suspend fun uploadPrescription(
        file: MultipartBody.Part,
        productId: RequestBody,
        productName: RequestBody,
        userId: RequestBody
    ): Response<PrescriptionModel> {
        Log.d(TAG, "========================================")
        Log.d(TAG, "Prescription upload started")
        Log.d(TAG, "File name: ${file.headers?.get("Content-Disposition")}")
        Log.d(TAG, "File content type: ${file.body.contentType()}")
        Log.d(TAG, "File size: ${file.body.contentLength()} bytes")
        Log.d(TAG, "Product ID: $productId")
        Log.d(TAG, "Product Name: $productName")
        Log.d(TAG, "User ID: $userId")
        Log.d(TAG, "========================================")

        try{
            val response = apiService.uploadPrescription(
                file = file,
                productId = productId,
                productName = productName,
                userId = userId
            )
            Log.d(TAG, "Response received")
            Log.d(TAG, "HTTP Code: ${response.code()}")
            Log.d(TAG, "Is Successful: ${response.isSuccessful}")

            if(response.isSuccessful){
                Log.d(TAG,"Success Body: ${response.body()}")
                Log.d(TAG,"Prescription Url: ${response.body()?.prescriptionUrl}")
                Log.d(TAG,"Public Id: ${response.body()?.publicId}")
            }else{
                val errorBody = response.errorBody()?.string()
                Log.e(TAG,"Upload Failed")
                Log.e(TAG,"HTTP Code: ${response.code()}")
                Log.e(TAG,"HTTP Message: ${response.message()}")
                Log.e(TAG,"Error Body: $errorBody")
            }
            return response
        }catch (e: Exception){
            Log.e(TAG, "========================================")
            Log.e(TAG, "Prescription upload exception")
            Log.e(TAG, "Exception type: ${e.javaClass.simpleName}")
            Log.e(TAG, "Exception message: ${e.message}")
            Log.e(TAG, "Localized message: ${e.localizedMessage}")
            Log.e(TAG, "========================================", e)

            throw e
        }
    }

}