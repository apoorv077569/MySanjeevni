package com.mysanjeevni.mysanjeevni.features.consult.presentation.state

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation

data class ConsultationDetailUiState(

    val consultation: Consultation? = null,

    val isCancelling: Boolean = false,
    val isLoading:Boolean = false,

    val isCancelled: Boolean = false,

    val error: String? = null,

    val message: String? = null
)
