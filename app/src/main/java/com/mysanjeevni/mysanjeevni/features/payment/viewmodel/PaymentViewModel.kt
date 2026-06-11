package com.mysanjeevni.mysanjeevni.features.payment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.CreateRazorpayOrderRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.PaymentRepository
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.RazorpayOrder
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.VerifyPaymentRequest
import com.mysanjeevni.mysanjeevni.features.payment.state.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    fun createRazorpayOrder(
        amount: Int,
        currency: String = "INR",
        receipt: String,
        notes: Map<String, String>? = null
    ) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            val request = CreateRazorpayOrderRequest(
                amount = amount,
                currency = currency,
                receipt = receipt,
                notes = notes
            )
            paymentRepository.createRazorpayOrder(request)
                .onSuccess { response ->
                    if (response.success && response.order != null) {
                        _paymentState.value = PaymentState.RazorpayOrderCreated(response.order)
                    } else {
                        _paymentState.value = PaymentState.Error("Failed to create payment order")
                    }
                }
                .onFailure { e ->
                    _paymentState.value = PaymentState.Error(e.message ?: "Something went wrong")
                }
        }
    }

    fun verifyPayment(
        razorpayOrderId: String,
        razorpayPaymentId: String,
        razorpaySignature: String
    ) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            val request = VerifyPaymentRequest(
                razorpayOrderId = razorpayOrderId,
                razorpayPaymentId = razorpayPaymentId,
                razorpaySignature = razorpaySignature
            )
            paymentRepository.verifyPayment(request)
                .onSuccess { response ->
                    if (response.success) {
                        _paymentState.value = PaymentState.PaymentSuccess
                    } else {
                        _paymentState.value = PaymentState.Error(response.message)
                    }
                }
                .onFailure { e ->
                    _paymentState.value = PaymentState.Error(e.message ?: "Something went wrong")
                }
        }
    }

    fun createOrderWithPayment(
        userId: String?,
        items: List<OrderItemDto>,
        totalPrice: Double,
        deliveryAddress: String,
        currency: String = "INR",
        notes: String? = null
    ) {
        viewModelScope.launch {
            Log.d("PAYMENT_VM", "========== CREATE ORDER START ==========")
            Log.d("PAYMENT_VM", "UserId: $userId")
            Log.d("PAYMENT_VM", "Items Count: ${items.size}")
            Log.d("PAYMENT_VM", "Items: $items")
            Log.d("PAYMENT_VM", "Total Price: $totalPrice")
            Log.d("PAYMENT_VM", "Delivery Address: $deliveryAddress")
            Log.d("PAYMENT_VM", "Currency: $currency")
            Log.d("PAYMENT_VM", "Notes: $notes")
            _paymentState.value = PaymentState.Loading
            val orderRequest = CreateOrderRequest(
                userId = userId,
                items = items,
                totalPrice = totalPrice,
                deliveryAddressId = deliveryAddress,
                currency = currency,
                notes = notes
            )
            Log.d("PAYMENT_VM", "Request Body: $orderRequest")
            orderRepository.createOrder(orderRequest)
                .onSuccess { response ->
                    Log.d("PAYMENT_VM", "========== API SUCCESS ==========")
                    Log.d("PAYMENT_VM", "Full Response: $response")
                    response.razorpayOrder?.let { razorpayOrder ->
                        Log.d(
                            "PAYMENT_VM",
                            """
                        Razorpay Order Details:
                        id = ${razorpayOrder.id}
                        amount = ${razorpayOrder.amount}
                        currency = ${razorpayOrder.currency}
                        receipt = ${razorpayOrder.receipt}
                        """.trimIndent()
                        )
                        _paymentState.value =
                            PaymentState.RazorpayOrderCreated(
                                RazorpayOrder(
                                    id = razorpayOrder.id,
                                    amount = razorpayOrder.amount,
                                    currency = razorpayOrder.currency,
                                    receipt = razorpayOrder.receipt
                                )
                            )
                    } ?: run {
                        Log.d(
                            "PAYMENT_VM",
                            "COD Order - No Razorpay Order Returned"
                        )
                        _paymentState.value = PaymentState.PaymentSuccess
                    }
                }
                .onFailure { e ->
                    Log.e("PAYMENT_VM", "========== API FAILED ==========")
                    Log.e("PAYMENT_VM", "Error Message: ${e.message}", e)
                    _paymentState.value =
                        PaymentState.Error(
                            e.message ?: "Something went wrong"
                        )
                }
        }
    }

    fun resetState() {
        _paymentState.value = PaymentState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(
            "PAYMENT_VM",
            "ViewModel Cleared"
        )
    }
}