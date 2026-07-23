package com.mysanjeevni.mysanjeevni.features.profile.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.data.mapper.toCreateRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.mapper.toUpdateRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository
import retrofit2.Response
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AddressRepository {

    override suspend fun fetchAddresses(
        token: String,
        userId: String
    ): Response<AddressResponseDto> {

        Log.d("ADDRESS_REPO_IMPL", "==============================")
        Log.d("ADDRESS_REPO_IMPL", "FETCH ADDRESSES")
        Log.d("ADDRESS_REPO_IMPL", "UserId = $userId")

        val response = apiService.getAddresses(
            token = "Bearer $token",
            userId = userId
        )

        Log.d(
            "ADDRESS_REPO_IMPL",
            "Fetch HTTP Code = ${response.code()}"
        )

        return response
    }

    override suspend fun addAddress(
        token: String,
        address: Address
    ): Response<AddressResponseDto> {

        val request = address.toCreateRequestDto()

        Log.d("ADDRESS_REPO_IMPL", "==============================")
        Log.d("ADDRESS_REPO_IMPL", "ADD ADDRESS")
        Log.d("ADDRESS_REPO_IMPL", "Request = $request")

        val response = apiService.addAddress(
            token = "Bearer $token",
            address = request
        )

        Log.d(
            "ADDRESS_REPO_IMPL",
            "Add HTTP Code = ${response.code()}"
        )

        return response
    }

    override suspend fun updateAddress(
        token: String,
        id: String,
        address: Address
    ): Response<AddressResponseDto> {

        val addressWithId = address.copy(
            id = id
        )

        val request = addressWithId.toUpdateRequestDto()

        Log.d("ADDRESS_REPO_IMPL", "==============================")
        Log.d("ADDRESS_REPO_IMPL", "UPDATE ADDRESS")
        Log.d("ADDRESS_REPO_IMPL", "AddressId = $id")
        Log.d("ADDRESS_REPO_IMPL", "Request = $request")

        val response = apiService.updateAddress(
            token = "Bearer $token",
            id = id,
            address = request
        )

        Log.d(
            "ADDRESS_REPO_IMPL",
            "Update HTTP Code = ${response.code()}"
        )

        return response
    }

    override suspend fun deleteAddress(
        token: String,
        userId: String,
        addressId: String
    ): Result<Unit> {

        return try {

            Log.d("ADDRESS_REPO_IMPL", "==============================")
            Log.d("ADDRESS_REPO_IMPL", "DELETE ADDRESS")
            Log.d("ADDRESS_REPO_IMPL", "AddressId = $addressId")
            Log.d("ADDRESS_REPO_IMPL", "UserId = $userId")

            val response = apiService.deleteAddress(
                token = "Bearer $token",
                id = addressId,
                userId = userId
            )

            Log.d(
                "ADDRESS_REPO_IMPL",
                "Delete HTTP Code = ${response.code()}"
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to delete address"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ADDRESS_REPO_IMPL",
                "Delete failed",
                e
            )

            Result.failure(e)
        }
    }
}