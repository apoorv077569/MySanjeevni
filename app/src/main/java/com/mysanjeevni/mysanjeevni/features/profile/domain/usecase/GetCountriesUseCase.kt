package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import javax.inject.Inject


import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Country
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository

class GetCountriesUseCase @Inject constructor(
    private val repository: AddressRepository
) {

    suspend operator fun invoke(): List<Country> {
        return repository.getCountries()
    }
}