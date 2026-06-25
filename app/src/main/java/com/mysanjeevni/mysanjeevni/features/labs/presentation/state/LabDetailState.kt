package com.mysanjeevni.mysanjeevni.features.labs.presentation.state

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTestDetail

data class LabDetailState(
    val isLoading: Boolean = false,
    val labTestDetail: LabTestDetail? = null,
    val error: String = ""
)
