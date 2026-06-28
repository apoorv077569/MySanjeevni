package com.mysanjeevni.mysanjeevni.features.orders.presentation.state

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderDetailDto
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem

data class OrderDetailState(
    val isLoading: Boolean = false,
    val order: RazorpayOrderDetailDto? = null,
    val medicines: List<Medicine> = emptyList(),
    val address: AddressItem? = null,
    val error: String? = null
)