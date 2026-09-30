package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.domain.model.State
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository
import javax.inject.Inject

class GetStatesUseCase @Inject constructor(
    private val repository: AddressRepository
) {

    suspend operator fun invoke(
        country: String
    ): List<State> {
        return repository.getStates(country)
    }
}