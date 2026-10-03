package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

data class ServiceabilityUiState(

    val isLoading: Boolean = false,

    val serviceable: Boolean = false,

    val courierName: String = "",

    val deliveryCharge: Double = 0.0,

    val estimatedDeliveryDate: String = "",

    val estimatedDeliveryDays: String = "",

    val codAvailable: Boolean = false,

    val error: String? = null
)