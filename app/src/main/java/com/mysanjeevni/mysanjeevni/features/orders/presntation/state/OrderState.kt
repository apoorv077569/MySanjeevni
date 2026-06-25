package com.mysanjeevni.mysanjeevni.features.orders.presntation.state

import com.mysanjeevni.mysanjeevni.features.orders.domain.model.OrderUiModel

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    data class Success(val orders: List<OrderUiModel>) : OrderState()
    data class Error(val message: String) : OrderState()
}