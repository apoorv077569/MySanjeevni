package com.mysanjeevni.mysanjeevni.features.prescription.data.repository

import android.net.Uri
import com.mysanjeevni.mysanjeevni.features.prescription.domain.repository.PrescriptionRepository
import android.content.Context
import com.mysanjeevni.mysanjeevni.data.remote.ApiClient
import com.mysanjeevni.mysanjeevni.data.remote.AuthApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PrescriptionRepositoryImpl  @Inject constructor(
    @ApplicationContext private val context: Context
) : PrescriptionRepository {

    override suspend fun uploadPrescription(imageUri: Uri): Result<String> {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val file = File(context.cacheDir, "prescription.jpg")
            file.outputStream().use { inputStream?.copyTo(it) }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val response = AuthApiClient.api.uploadPrescription(imagePart)

            if (response.isSuccessful) {
                val url = response.body()?.url ?: ""
                Result.success(url)
            } else {
                Result.failure(Exception("Upload failed: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }}