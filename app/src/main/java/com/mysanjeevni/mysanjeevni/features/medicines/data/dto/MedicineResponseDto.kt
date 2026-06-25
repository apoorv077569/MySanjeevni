package com.mysanjeevni.mysanjeevni.features.medicines.data.dto
data class MedicineResponseDto(
    val message: String,
    val products: List<MedicineDto>,
    val pagination: PaginationDto
)