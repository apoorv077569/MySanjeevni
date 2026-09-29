package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.state

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine

data class PharmacyState(
    val isLoading: Boolean = false,
    val medicines: List<Medicine> = emptyList(),

    // Total medicines available on backend
    val totalMedicines: Int = 0,

    // All unique medicine categories
    val categories: List<String> = emptyList(),

    val isLoadingMore: Boolean = false,
    val hasMorePages: Boolean = true,
    val currentPage: Int = 0,
    val error: String = ""
)