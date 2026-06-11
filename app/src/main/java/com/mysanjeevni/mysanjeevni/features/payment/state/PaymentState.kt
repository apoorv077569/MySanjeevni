package com.mysanjeevni.mysanjeevni.features.payment.state

import com.mysanjeevni.mysanjeevni.features.payment.data.remote.RazorpayOrder
import com.mysanjeevni.mysanjeevni.features.payment.domain.model.PaymentMethod

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class RazorpayOrderCreated(val order: RazorpayOrder) : PaymentState()
    object PaymentSuccess : PaymentState()
    data class Error(val message: String) : PaymentState()
}
