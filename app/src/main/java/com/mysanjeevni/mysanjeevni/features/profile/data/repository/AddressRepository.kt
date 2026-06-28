package com.mysanjeevni.mysanjeevni.features.profile.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.model.address.AddressModel
import com.mysanjeevni.mysanjeevni.data.remote.model.address.toCreateRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.address.toUpdateRequest
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchAddresses(token: String, userId: String): List<AddressItem> {
        Log.d("ADDRESS_REPO", "📡 Fetching addresses...")

        val response = apiService.getAddresses("Bearer $token", userId)

        Log.d("ADDRESS_REPO", "==============================")
        Log.d("ADDRESS_REPO", "📡 GET ADDRESSES RESPONSE")
        Log.d("ADDRESS_REPO", "Code: ${response.code()}")
        Log.d("ADDRESS_REPO", "Message: ${response.message()}")
        Log.d("ADDRESS_REPO", "Headers: ${response.headers()}")

        if (response.isSuccessful) {
            val body = response.body()

            Log.d("ADDRESS_REPO", "✅ SUCCESS BODY:")
            Log.d("ADDRESS_REPO", "Message: ${body?.message}")
            Log.d("ADDRESS_REPO", "Total: ${body?.total}")
            Log.d("ADDRESS_REPO", "Addresses: ${body?.addresses}")

            body?.addresses?.forEach {
                Log.d("ADDRESS_REPO", "➡️ ${it.fullName}, ${it.city}, ${it.id}")
            }

        } else {
            val errorBody = response.errorBody()?.string()

            Log.e("ADDRESS_REPO", "❌ ERROR BODY:")
            Log.e("ADDRESS_REPO", errorBody ?: "NULL ERROR BODY")
        }
        return response.body()?.addresses ?: emptyList()
    }


    suspend fun addAddress(token: String, address: AddressModel) {
        val request = address.toCreateRequest()
        Log.d("ADDRESS_REPO", "📤 Adding address...")

        val response = apiService.addAddress("Bearer $token", request)

        Log.d("ADDRESS_REPO", "==============================")
        Log.d("ADDRESS_REPO", "➕ ADD ADDRESS RESPONSE")
        Log.d("ADDRESS_REPO", "Code: ${response.code()}")
        val body = response.body()
        Log.d("ADDRESS_REPO", "BODY: $body")
        Log.d("ADDRESS_REPO", "Request Body: $request")

        if (response.isSuccessful) {
            Log.d("ADDRESS_REPO", "✅ Added Address:")
            Log.d("ADDRESS_REPO", "Message: ${body?.message}")
            Log.d("ADDRESS_REPO", "Address: ${body?.addresses}")
        } else {
            Log.e("ADDRESS_REPO", "❌ Error: ${response.errorBody()?.string()}")
        }
    }


    suspend fun updateAddress(
        token: String,
        id: String,
        address: AddressModel
    ) {
        val request = address.toUpdateRequest()
        Log.d("ADDRESS_REPO", "✏️ Updating address...")

        val response = apiService.updateAddress("Bearer $token", id, request)

        Log.d("ADDRESS_REPO", "==============================")
        Log.d("ADDRESS_REPO", "✏️ UPDATE RESPONSE")
        Log.d("ADDRESS_REPO", "Code: ${response.code()}")

        if (response.isSuccessful) {
            val body = response.body()
            Log.d("ADDRESS_REPO", "✅ Updated:")
            Log.d("ADDRESS_REPO", "Message: ${body?.message}")
            Log.d("ADDRESS_REPO", "Address: ${body?.addresses}")
        } else {
            Log.e("ADDRESS_REPO", "❌ Error: ${response.errorBody()?.string()}")
        }
    }


    suspend fun deleteAddress(
        token: String,
        userId: String,
        addressId: String
    ) {
        Log.d("ADDRESS_REPO", "🗑️ Deleting address...")

        val response = apiService.deleteAddress("Bearer $token", addressId, userId)

        Log.d("ADDRESS_REPO", "==============================")
        Log.d("ADDRESS_REPO", "🗑 DELETE RESPONSE")
        Log.d("ADDRESS_REPO", "Code: ${response.code()}")

        if (response.isSuccessful) {
            val body = response.body()
            Log.d("ADDRESS_REPO", "✅ Deleted:")
        } else {
            Log.e("ADDRESS_REPO", "❌ Error: ${response.errorBody()?.string()}")
        }
    }
}