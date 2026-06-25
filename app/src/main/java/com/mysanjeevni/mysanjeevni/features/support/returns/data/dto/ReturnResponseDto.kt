package com.mysanjeevni.mysanjeevni.features.support.returns.data.dto

data class ReturnResponseDto(
    val message: String,
    val request: ReturnDto
)

data class ReturnListResponseDto(
    val requests: List<ReturnDto>
)