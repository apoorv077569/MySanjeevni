package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address

data class AddressUiState(
    val isLoading: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val error: String? = null,
    val successMessage: String? = null
)