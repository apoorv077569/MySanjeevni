package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(
        token: String,
        id: String,
        address: Address
    ): Response<AddressResponseDto> {
        return repository.updateAddress(
            token = token,
            id = id,
            address = address
        )
    }
}