package com.mysanjeevni.mysanjeevni.features.profile.data.mapper

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.CountryDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Country

fun CountryDto.toDomain(): Country {
    return Country(
        name = country.orEmpty(),
        iso2 = iso2,
        iso3 = iso3
    )
}