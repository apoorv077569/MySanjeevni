package com.mysanjeevni.mysanjeevni.features.prescription.presentation.state

import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Prescription
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.PrescriptionModel

data class PrescriptionState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val prescriptions: List<Prescription> = emptyList(),
    val data: PrescriptionModel? = null,
    val error: String? = null
)