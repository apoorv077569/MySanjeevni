package com.mysanjeevni.mysanjeevni.features.prescription.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.prescription.domain.usecase.UploadPrescriptionUseCase
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.state.PrescriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val uploadPrescriptionUseCase: UploadPrescriptionUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "PRESCRIPTION_VM"
    }

    private val _uiState =
        MutableStateFlow(PrescriptionState())

    val uiState: StateFlow<PrescriptionState> =
        _uiState.asStateFlow()

    fun uploadPrescription(
        context: Context,
        fileUri: Uri,
        productId: String,
        productName: String,
        userId: String
    ) {
        viewModelScope.launch {

            Log.d(TAG, "========================================")
            Log.d(TAG, "Upload started")
            Log.d(TAG, "URI: $fileUri")
            Log.d(TAG, "Product ID: $productId")
            Log.d(TAG, "Product Name: $productName")
            Log.d(TAG, "User ID: $userId")
            Log.d(TAG, "========================================")

            _uiState.value = PrescriptionState(
                isLoading = true
            )

            try {
                // 1. Get MIME type
                val mimeType =
                    context.contentResolver.getType(fileUri)
                        ?: "application/octet-stream"

                Log.d(TAG, "Mime Type: $mimeType")

                // 2. Validate MIME type
                val allowedTypes = listOf(
                    "image/jpeg",
                    "image/png",
                    "application/pdf"
                )

                if (mimeType !in allowedTypes) {
                    throw IllegalArgumentException(
                        "Invalid file type. Allowed: jpg, jpeg, png, pdf"
                    )
                }

                // 3. Read selected file
                val inputStream =
                    context.contentResolver.openInputStream(fileUri)
                        ?: throw IllegalArgumentException(
                            "Unable to open selected file"
                        )

                val fileBytes = inputStream.use {
                    it.readBytes()
                }

                Log.d(TAG, "File size: ${fileBytes.size} bytes")

                // 4. Validate max 5MB
                val maxSize = 5 * 1024 * 1024

                if (fileBytes.size > maxSize) {
                    throw IllegalArgumentException(
                        "File size exceeds 5MB limit"
                    )
                }

                // 5. Get actual filename
                val fileName = getFileName(
                    context = context,
                    uri = fileUri
                ) ?: "prescription_${System.currentTimeMillis()}"

                Log.d(TAG, "File Name: $fileName")

                // 6. Convert file bytes to RequestBody
                val fileRequestBody =
                    fileBytes.toRequestBody(
                        mimeType.toMediaTypeOrNull()
                    )

                // 7. Create Multipart file
                val filePart =
                    MultipartBody.Part.createFormData(
                        name = "file",
                        filename = fileName,
                        body = fileRequestBody
                    )

                // 8. Create text RequestBody fields
                val productIdBody =
                    productId.toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )

                val productNameBody =
                    productName.toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )

                val userIdBody =
                    userId.toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )

                Log.d(TAG, "Multipart request created")
                Log.d(TAG, "Calling UploadPrescriptionUseCase")

                // 9. Call UseCase
                val response = uploadPrescriptionUseCase(
                    file = filePart,
                    productId = productIdBody,
                    productName = productNameBody,
                    userId = userIdBody
                )

                Log.d(TAG, "Response Code: ${response.code()}")
                Log.d(TAG, "Successful: ${response.isSuccessful}")

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {
                        Log.d(TAG, "Upload successful")
                        Log.d(
                            TAG,
                            "Prescription URL: ${body.prescriptionUrl}"
                        )
                        Log.d(
                            TAG,
                            "Public ID: ${body.publicId}"
                        )

                        _uiState.value = PrescriptionState(
                            isLoading = false,
                            isSuccess = true,
                            data = body,
                            error = null
                        )
                    } else {
                        Log.e(TAG, "Success response body is null")

                        _uiState.value = PrescriptionState(
                            isLoading = false,
                            error = "Empty server response"
                        )
                    }

                } else {
                    val errorBody =
                        response.errorBody()?.string()

                    Log.e(TAG, "Upload failed")
                    Log.e(TAG, "Code: ${response.code()}")
                    Log.e(TAG, "Error Body: $errorBody")

                    _uiState.value = PrescriptionState(
                        isLoading = false,
                        error = errorBody
                            ?: "Prescription upload failed"
                    )
                }

            } catch (e: Exception) {

                Log.e(TAG, "========================================")
                Log.e(TAG, "Upload exception")
                Log.e(TAG, "Type: ${e.javaClass.simpleName}")
                Log.e(TAG, "Message: ${e.message}")
                Log.e(TAG, "========================================", e)

                _uiState.value = PrescriptionState(
                    isLoading = false,
                    isSuccess = false,
                    error = e.localizedMessage
                        ?: "Prescription upload failed"
                )
            }
        }
    }

    private fun getFileName(
        context: Context,
        uri: Uri
    ): String? {

        var fileName: String? = null

        try {
            if (uri.scheme == "content") {
                context.contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                )?.use { cursor ->

                    val nameIndex =
                        cursor.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                        )

                    if (
                        cursor.moveToFirst() &&
                        nameIndex >= 0
                    ) {
                        fileName =
                            cursor.getString(nameIndex)
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(
                TAG,
                "Unable to read file name: ${e.message}"
            )
        }

        return fileName ?: uri.lastPathSegment
    }

    fun resetState() {
        Log.d(TAG, "Resetting UI state")

        _uiState.value = PrescriptionState()
    }
}