package com.mysanjeevni.mysanjeevni.features.profile.domain.repository

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
import retrofit2.Response

interface AddressRepository {

    suspend fun fetchAddresses(
        token: String,
        userId: String
    ): Response<AddressResponseDto>

    suspend fun addAddress(
        token: String,
        address: Address
    ): Response<AddressResponseDto>

    suspend fun updateAddress(
        token: String,
        id: String,
        address: Address
    ): Response<AddressResponseDto>

    suspend fun deleteAddress(
        token: String,
        userId: String,
        addressId: String
    ): Result<Unit>
}