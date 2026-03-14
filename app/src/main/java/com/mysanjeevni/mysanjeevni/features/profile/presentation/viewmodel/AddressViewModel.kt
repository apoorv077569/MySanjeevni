package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddressState())
    val state: StateFlow<AddressState> = _state.asStateFlow()

    init {
        loadAddress()
    }

    private fun loadAddress() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(500)

            val mockList = listOf(
                AddressItem(
                    "1",
                    "Home",
                    "John Doe",
                    "+91 9876543210",
                    "Falt 401 Sunshine Apartment, MG Road",
                    "Banglore, Karnataka - 560001",
                    true
                ),
                AddressItem(
                    "2",
                    "Work",
                    "John Doe",
                    "+91 9876543210",
                    "Tech Park, Sector 5, Salt Lake",
                    "Kolkata, West Bengal - 700091",
                    false
                )
            )
            _state.update { it.copy(isLoading = false, addresses = mockList) }
        }
    }
    fun deleteAddress(id: String){
        val currentList = _state.value.addresses.toMutableList()
        currentList.removeAll{it.id == id}
        _state.update { it.copy(addresses = currentList) }
    }
    fun setDefaultAddress(id:String){
        val updatedList = _state.value.addresses.map {
            it.copy(isDefault = it.id == id)
        }
        _state.update { it.copy(addresses = updatedList) }
    }
}