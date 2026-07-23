package com.mysanjeevni.mysanjeevni.features.medicines.presentation.state

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine

data class MedicineState(

    val isLoading: Boolean = false,

    val medicines: List<Medicine> = emptyList(),

    val selectedMedicine: Medicine? = null,

    val error: String? = null,
    val isLoadingMore:Boolean = false,
    val hasMorePages:Boolean = true,
    val currentPage:Int = 0,
    )