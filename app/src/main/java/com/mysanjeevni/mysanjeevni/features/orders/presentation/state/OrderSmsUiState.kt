package com.mysanjeevni.mysanjeevni.features.orders.presentation.state

sealed class OrderSmsUiState {

    data object Idle : OrderSmsUiState()

    data object Loading : OrderSmsUiState()

    data class Success(
        val message: String
    ) : OrderSmsUiState()

    data class Error(
        val message: String
    ) : OrderSmsUiState()
}