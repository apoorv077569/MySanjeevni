package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.remote.model.AddressModel
import com.mysanjeevni.mysanjeevni.data.remote.model.CreateAddressRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.UpdateAddressRequest
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressState
import com.mysanjeevni.mysanjeevni.features.profile.data.repository.AddressRepository
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    application: Application,
    private val repository: AddressRepository,
    private val sessionManager: SessionManager
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AddressState())
    val state: StateFlow<AddressState> = _state.asStateFlow()

    init {
        loadAddresses()
    }

    // 🔹 LOAD
    fun loadAddresses() {
        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        Log.d("ADDRESS_VM", "🚀 LOAD ADDRESSES")
        Log.d("ADDRESS_VM", "Token: $token")
        Log.d("ADDRESS_VM", "UserId: $userId")

        if (token.isNullOrBlank() || userId.isNullOrBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val list = repository.fetchAddresses(token, userId)

                _state.update {
                    it.copy(
                        addresses = list,
                        isLoading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    // 🔹 ADD
    fun addAddress(address: AddressModel) {
        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        if (token.isNullOrBlank() || userId.isNullOrBlank()) return

        viewModelScope.launch {
            try {
                val newAddress = address.copy(userId = userId)

                repository.addAddress(token, newAddress)

                Log.d("ADDRESS_VM", "✅ Address Added")

                loadAddresses()

            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    // 🔹 UPDATE
    fun updateAddress(id: String, address: AddressModel) {
        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        if (token.isNullOrBlank() || userId.isNullOrBlank()) return

        viewModelScope.launch {
            try {
                val updated = address.copy(userId = userId)

                repository.updateAddress(token, id, updated)

                Log.d("ADDRESS_VM", "✅ Address Updated")

                loadAddresses()

            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    // 🔹 DELETE
    fun deleteAddress(addressId: String) {
        val token = sessionManager.getToken()
        val userId = sessionManager.getUserId()

        if (token.isNullOrBlank() || userId.isNullOrBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                repository.deleteAddress(token, userId, addressId)

                Log.d("ADDRESS_VM", "✅ Address Deleted")

                loadAddresses()

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    // 🔹 SELECT
    fun selectAddress(address: AddressItem) {
        _state.update { it.copy(selectedAddress = address) }
    }
}