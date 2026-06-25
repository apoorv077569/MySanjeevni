package com.mysanjeevni.mysanjeevni.features.labs.data.dto

data class LabTestsResponseDto(
    val message: String,
    val tests: List<LabTestDto>,
    val pagination: PaginationDto
)