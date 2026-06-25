package com.mysanjeevni.mysanjeevni.features.support.returns.presentation.state

import com.mysanjeevni.mysanjeevni.features.support.returns.domain.model.ReturnRequest

data class ReturnUiState(
    val isLoading: Boolean = false,
    val returns: List<ReturnRequest> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)