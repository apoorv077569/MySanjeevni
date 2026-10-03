package com.mysanjeevni.mysanjeevni.features.currency.data.dto

data class CountryResponseDto(
    val success: Boolean? = true,
    val country_code: String?,
    val country: String? = null,
    val message: String? = null // present only when success = false
)