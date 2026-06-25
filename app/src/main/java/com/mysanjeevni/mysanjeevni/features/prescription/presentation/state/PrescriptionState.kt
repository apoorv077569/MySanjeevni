package com.mysanjeevni.mysanjeevni.features.prescription.presentation.state

import android.net.Uri

data class PrescriptionState(
    val selectedImageUri: Uri? = null,
    val isUploading: Boolean = false,
    val isUploaded: Boolean = false,
    val error: String? = null
)