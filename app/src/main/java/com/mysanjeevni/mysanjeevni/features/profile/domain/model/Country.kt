package com.mysanjeevni.mysanjeevni.features.profile.domain.model

data class Country(
    val name: String,
    val iso2: String? = null,
    val iso3: String? = null
)