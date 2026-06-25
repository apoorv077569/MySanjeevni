package com.mysanjeevni.mysanjeevni.features.prescription.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.prescription.domain.repository.PrescriptionRepository
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.state.PrescriptionState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.mysanjeevni.mysanjeevni.features.prescription.data.repository.PrescriptionRepositoryImpl

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val repository: PrescriptionRepositoryImpl,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(PrescriptionState())
    val state = _state.asStateFlow()

    fun selectImage(uri: Uri) {
        _state.update { it.copy(selectedImageUri = uri) }
    }

    fun uploadPrescription(context: Context) {
        val imageUri = _state.value.selectedImageUri ?: return

        viewModelScope.launch {
            _state.update { it.copy(isUploading = true, error = null) }
            val result = repository.uploadPrescription(imageUri)
            result.onSuccess { cloudinaryUrl ->
                Log.d("PRESCRIPTION", "Uploaded URL: $cloudinaryUrl")
                sendToWhatsApp(context, cloudinaryUrl)
                _state.update {
                    it.copy(isUploading = false, isUploaded = true)
                }
            }.onFailure { error ->
                Log.e("PRESCRIPTION", "Upload failed: ${error.message}")
                _state.update {
                    it.copy(isUploading = false, error = error.message)
                }
            }
        }
    }

    private fun sendToWhatsApp(context: Context, imageUrl: String) {
        val userAddress = sessionManager.getUserAddress()
        val userName = sessionManager.getUserName()

        val message = """
            🏥 *New Prescription Order*
            👤 *Name:* $userName
            🆔 *UserAddress:* $userAddress
            📋 *Prescription:* $imageUrl
            
            Please process this order.
        """.trimIndent()

        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
            data = "https://wa.me/919634607848?text=${Uri.encode(message)}".toUri()
        }
        context.startActivity(intent)
    }
}