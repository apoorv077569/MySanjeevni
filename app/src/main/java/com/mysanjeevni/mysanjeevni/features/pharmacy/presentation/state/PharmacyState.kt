package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.state

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine


data class PharmacyState(
    val isLoading: Boolean = false,
    val medicines: List<Medicine> = emptyList(),
    val isLoadingMore:Boolean = false,
    val hasMorePages:Boolean = true,
    val currentPage:Int = 0,
    val error: String = ""
)
