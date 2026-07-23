package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository
import retrofit2.Response
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(
        token: String,
        userId: String
    ): Response<AddressResponseDto> {
        return repository.fetchAddresses(
            token = token,
            userId = userId
        )
    }
}