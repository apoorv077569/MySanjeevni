package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

//import android.app.Application
//import android.util.Log
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.mysanjeevni.mysanjeevni.data.remote.model.address.AddressModel
//import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
//import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressState
//import com.mysanjeevni.mysanjeevni.features.profile.data.repository.AddressRepository
//import com.mysanjeevni.mysanjeevni.utils.SessionManager
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class AddressViewModel @Inject constructor(
//    application: Application,
//    private val repository: AddressRepository,
//    private val sessionManager: SessionManager
//) : AndroidViewModel(application) {
//
//    private val _state = MutableStateFlow(AddressState())
//    val state: StateFlow<AddressState> = _state.asStateFlow()
//
//    init {
//        loadAddresses()
//    }
//
//    fun loadAddresses() {
//        val token = sessionManager.getToken()
//        val userId = sessionManager.getUserId()
//
//        Log.d("ADDRESS_VM", "🚀 LOAD ADDRESSES")
//        Log.d("ADDRESS_VM", "Token: $token")
//        Log.d("ADDRESS_VM", "UserId: $userId")
//
//        if (token.isNullOrBlank() || userId.isNullOrBlank()) return
//
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
//
//            try {
//                val list = repository.fetchAddresses(token, userId)
//
//                _state.update {
//                    it.copy(
//                        addresses = list,
//                        isLoading = false,
//                        error = null
//                    )
//                }
//
//            } catch (e: Exception) {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        error = e.message
//                    )
//                }
//            }
//        }
//    }
//
//    // 🔹 ADD
//    fun addAddress(address: AddressModel) {
//        val token = sessionManager.getToken()
//        val userId = sessionManager.getUserId()
//
//        if (token.isNullOrBlank() || userId.isNullOrBlank()) return
//
//        viewModelScope.launch {
//            try {
//                val newAddress = address.copy(userId = userId)
//
//                repository.addAddress(token, newAddress)
//
//                Log.d("ADDRESS_VM", "✅ Address Added")
//
//                loadAddresses()
//
//            } catch (e: Exception) {
//                _state.update { it.copy(error = e.message) }
//            }
//        }
//    }
//
//    // 🔹 UPDATE
//    fun updateAddress(id: String, address: AddressModel) {
//        val token = sessionManager.getToken()
//        val userId = sessionManager.getUserId()
//
//        if (token.isNullOrBlank() || userId.isNullOrBlank()) return
//
//        viewModelScope.launch {
//            try {
//                val updated = address.copy(userId = userId)
//
//                repository.updateAddress(token, id, updated)
//
//                Log.d("ADDRESS_VM", "✅ Address Updated")
//
//                loadAddresses()
//
//            } catch (e: Exception) {
//                _state.update { it.copy(error = e.message) }
//            }
//        }
//    }
//
//    // 🔹 DELETE
//    fun deleteAddress(addressId: String) {
//        val token = sessionManager.getToken()
//        val userId = sessionManager.getUserId()
//
//        if (token.isNullOrBlank() || userId.isNullOrBlank()) return
//
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
//
//            try {
//                repository.deleteAddress(token, userId, addressId)
//
//                Log.d("ADDRESS_VM", "✅ Address Deleted")
//
//                loadAddresses()
//
//            } catch (e: Exception) {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        error = e.message
//                    )
//                }
//            }
//        }
//    }
//
//    // 🔹 SELECT
//    fun selectAddress(address: AddressItem) {
//        _state.update { it.copy(selectedAddress = address) }
//    }
//}


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.AddAddressUseCase
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.DeleteAddressUseCase
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.GetAddressUseCase
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.UpdateAddressUseCase
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressUiState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    application: Application,
    private val getAddressUseCase: GetAddressUseCase,
    private val addAddressUseCase: AddAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val sessionManager: SessionManager
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(
        AddressUiState()
    )

    val state: StateFlow<AddressUiState> =
        _state.asStateFlow()

    init {
        loadAddresses()
    }

    fun loadAddresses() {

        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        Log.d("ADDRESS_VM", "==============================")
        Log.d("ADDRESS_VM", "LOAD ADDRESSES")
        Log.d("ADDRESS_VM", "UserId = $userId")

        if (token.isNullOrBlank() || userId.isNullOrBlank()) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "User session not found"
                )
            }
            return
        }

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {

                val response = getAddressUseCase(
                    token = token,
                    userId = userId
                )

                Log.d(
                    "ADDRESS_VM",
                    "GET HTTP Code = ${response.code()}"
                )

                if (response.isSuccessful) {

                    val addresses = response.body()
                        ?.addresses
                        ?.map { dto ->
                            Address(
                                id = dto.id.orEmpty(),
                                userId = dto.userId.orEmpty(),
                                type = dto.type.orEmpty(),
                                fullName = dto.fullName.orEmpty(),
                                phone = dto.phone.orEmpty(),
                                addressLine1 = dto.addressLine1.orEmpty(),
                                addressLine2 = dto.addressLine2.orEmpty(),
                                city = dto.city.orEmpty(),
                                state = dto.state.orEmpty(),
                                pincode = dto.pincode.orEmpty(),
                                country = dto.country ?: "India",
                                isDefault = dto.isDefault ?: false,
                                createdAt = dto.createdAt.orEmpty(),
                                updatedAt = dto.updatedAt.orEmpty()
                            )
                        }
                        ?: emptyList()

                    _state.update {
                        it.copy(
                            addresses = addresses,
                            isLoading = false,
                            error = null
                        )
                    }

                    Log.d(
                        "ADDRESS_VM",
                        "Loaded ${addresses.size} addresses"
                    )

                } else {

                    val errorMessage =
                        response.errorBody()?.string()
                            ?: "Failed to load addresses"

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                }

            } catch (e: Exception) {

                Log.e(
                    "ADDRESS_VM",
                    "Load failed",
                    e
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun addAddress(address: Address) {

        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        if (token.isNullOrBlank() || userId.isNullOrBlank()) {
            _state.update {
                it.copy(error = "User session not found")
            }
            return
        }

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null
                )
            }

            try {

                val newAddress = address.copy(
                    userId = userId
                )

                val response = addAddressUseCase(
                    token = token,
                    address = newAddress
                )

                Log.d(
                    "ADDRESS_VM",
                    "ADD HTTP Code = ${response.code()}"
                )

                if (response.isSuccessful) {

                    _state.update {
                        it.copy(
                            successMessage = response.body()?.message
                                ?: "Address added successfully"
                        )
                    }

                    loadAddresses()

                } else {

                    val errorMessage =
                        response.errorBody()?.string()
                            ?: "Failed to add address"

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                }

            } catch (e: Exception) {

                Log.e(
                    "ADDRESS_VM",
                    "Add failed",
                    e
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun updateAddress(
        id: String,
        address: Address
    ) {

        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        if (token.isNullOrBlank() || userId.isNullOrBlank()) {
            _state.update {
                it.copy(error = "User session not found")
            }
            return
        }

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null
                )
            }

            try {

                val updatedAddress = address.copy(
                    id = id,
                    userId = userId
                )

                val response = updateAddressUseCase(
                    token = token,
                    id = id,
                    address = updatedAddress
                )

                Log.d(
                    "ADDRESS_VM",
                    "UPDATE HTTP Code = ${response.code()}"
                )

                if (response.isSuccessful) {

                    _state.update {
                        it.copy(
                            successMessage = response.body()?.message
                                ?: "Address updated successfully"
                        )
                    }

                    loadAddresses()

                } else {

                    val errorMessage =
                        response.errorBody()?.string()
                            ?: "Failed to update address"

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                }

            } catch (e: Exception) {

                Log.e(
                    "ADDRESS_VM",
                    "Update failed",
                    e
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun deleteAddress(addressId: String) {

        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        if (token.isNullOrBlank() || userId.isNullOrBlank()) {
            _state.update {
                it.copy(error = "User session not found")
            }
            return
        }

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null
                )
            }

            val result = deleteAddressUseCase(
                token = token,
                userId = userId,
                addressId = addressId
            )

            result
                .onSuccess {

                    Log.d(
                        "ADDRESS_VM",
                        "Address deleted successfully"
                    )

                    _state.update {
                        it.copy(
                            successMessage = "Address deleted successfully"
                        )
                    }

                    loadAddresses()
                }
                .onFailure { exception ->

                    Log.e(
                        "ADDRESS_VM",
                        "Delete failed",
                        exception
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to delete address"
                        )
                    }
                }
        }
    }

    fun selectAddress(address: Address) {
        _state.update {
            it.copy(
                selectedAddress = address
            )
        }
    }

    fun clearError() {
        _state.update {
            it.copy(error = null)
        }
    }

    fun clearSuccessMessage() {
        _state.update {
            it.copy(successMessage = null)
        }
    }
}