package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(
        token: String,
        userId: String,
        addressId: String
    ): Result<Unit> {
        return repository.deleteAddress(
            token = token,
            userId = userId,
            addressId = addressId
        )
    }
}