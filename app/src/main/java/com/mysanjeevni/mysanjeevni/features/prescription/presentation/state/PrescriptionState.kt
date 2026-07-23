package com.mysanjeevni.mysanjeevni.features.prescription.presentation.state

import android.net.Uri
import com.mysanjeevni.mysanjeevni.features.prescription.data.model.PrescriptionModel

data class PrescriptionState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val data: PrescriptionModel? = null,
    val error: String? = null
)