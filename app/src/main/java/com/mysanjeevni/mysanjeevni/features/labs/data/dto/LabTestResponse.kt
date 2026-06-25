package com.mysanjeevni.mysanjeevni.features.labs.data.dto

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest

data class LabTestsResponse(
    val message: String?,
    val tests: List<LabTestDto>?,
    val pagination: PaginationDto? // 👈 optional
)

data class LabTestsPage(
    val tests:List<LabTest>,
    val pagination: PaginationDto?
)


