package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountriesResponseDto(
    val error: Boolean,
    val msg: String,
    val data: List<CountryDto>
)

@Serializable
data class CountryDto(
    val country: String? = null,
    val iso2: String? = null,
    val iso3: String? = null,
    val cities: List<String>? = null
)
