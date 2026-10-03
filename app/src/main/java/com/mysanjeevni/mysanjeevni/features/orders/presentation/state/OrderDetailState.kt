package com.mysanjeevni.mysanjeevni.features.orders.presentation.state

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderDetailDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address


data class OrderDetailState(
    val isLoading: Boolean = false,
    val order: RazorpayOrderDetailDto? = null,
    val medicines: List<Medicine> = emptyList(),
    val address: Address? = null,
    val error: String? = null,
    val isCancelling: Boolean = false,
    val cancelMessage: String? = null
)

