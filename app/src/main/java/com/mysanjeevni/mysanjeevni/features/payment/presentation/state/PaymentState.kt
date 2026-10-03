package com.mysanjeevni.mysanjeevni.features.payment.presentation.state

import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.RazorpayOrder

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class RazorpayOrderCreated(val order: RazorpayOrder) : PaymentState()
    object PaymentVerified : PaymentState()
    data class CodOrderCreated(val order: Order) : PaymentState()

    object PaymentSuccess : PaymentState()
    data class Error(val message: String) : PaymentState()
}
