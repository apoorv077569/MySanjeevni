package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.state

import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model.Medicine

data class PharamcyState(
    val isLoading: Boolean = false,
    val medicines: List<Medicine> = emptyList(),
    val error: String = ""
)
