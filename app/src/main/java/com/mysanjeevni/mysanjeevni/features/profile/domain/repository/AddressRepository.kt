package com.mysanjeevni.mysanjeevni.features.profile.domain.repository

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Country
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.State
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
    suspend fun getCountries(): List<Country>

    suspend fun getStates(
        country: String
    ): List<State>
}