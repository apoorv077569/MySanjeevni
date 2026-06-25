package com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot

data class ServiceabilityResponseDto(
    val provider: String,
    val pincode: String,
    val isServiceable: Boolean,
    val serviceTypes: List<String>
)