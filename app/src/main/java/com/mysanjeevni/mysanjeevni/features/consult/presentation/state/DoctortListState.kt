package com.mysanjeevni.mysanjeevni.features.consult.presentation.state

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor

data class DoctorListState(
    val isLoading: Boolean = false,
    val doctors: List<Doctor> = emptyList(),
    val error: String? = null
)
