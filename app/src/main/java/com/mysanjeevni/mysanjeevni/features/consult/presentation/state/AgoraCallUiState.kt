package com.mysanjeevni.mysanjeevni.features.consult.presentation.state

import com.mysanjeevni.mysanjeevni.features.consult.data.dto.AgoraTokenResponseDto

data class AgoraCallUiState(
    val isLoading: Boolean = false,
    val tokenData: AgoraTokenResponseDto? = null,
    val error: String? = null
)