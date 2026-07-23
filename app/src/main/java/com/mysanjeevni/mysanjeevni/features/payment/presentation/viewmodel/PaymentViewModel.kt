package com.mysanjeevni.mysanjeevni.features.payment.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.CreateRazorpayOrderRequest
import com.mysanjeevni.mysanjeevni.features.payment.domain.repository.PaymentRepository
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.RazorpayOrder
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.VerifyPaymentRequest
import com.mysanjeevni.mysanjeevni.features.payment.presentation.state.PaymentState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val sessionManager: SessionManager

) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()
    private val userId = sessionManager.getUserId()
    private var razorpayOrderId: String = ""
    private var razorpayPaymentId: String = ""
    private var razorpaySignature: String = ""

    private var shippingCharge: Double = 0.0

    fun createRazorpayOrder(
        amount: Int,
        currency: String = "INR",
        receipt: String,
        notes: Map<String, String>? = null
    ) {
        Log.d("PAYMENT_VM", "FUNCTION CALLED")
        viewModelScope.launch {
            Log.d("PAYMENT_VM", "COROUTINE STARTED")
            _paymentState.value = PaymentState.Loading
            val request = CreateRazorpayOrderRequest(
                amount = amount,
                currency = currency,
                receipt = receipt,
                notes = notes
            )
            Log.d("PAYMENT_VM", "REQUEST = $request")
            paymentRepository.createRazorpayOrder(request)
                .onSuccess { response ->
                    if (response.success && response.order != null) {
                        _paymentState.value = PaymentState.RazorpayOrderCreated(response.order)
                        Log.d("PAYMENT_VM", "SUCCESS")
                    } else {
                        _paymentState.value = PaymentState.Error("Failed to create payment order")
                        Log.e("PAYMENT_VM", "FAILURE")

                    }
                }
                .onFailure { e ->
                    _paymentState.value = PaymentState.Error(e.message ?: "Something went wrong")
                }
        }
    }

    fun getRazorpayOrderId() = razorpayOrderId

    fun getRazorpayPaymentId() = razorpayPaymentId

    fun getRazorpaySignature() = razorpaySignature

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

                        this@PaymentViewModel.razorpayOrderId = razorpayOrderId
                        this@PaymentViewModel.razorpayPaymentId = razorpayPaymentId
                        this@PaymentViewModel.razorpaySignature = razorpaySignature

                        Log.d("PAYMENT_VM", "OrderId = $razorpayOrderId")
                        Log.d("PAYMENT_VM", "PaymentId = $razorpayPaymentId")
                        Log.d("PAYMENT_VM", "Signature = $razorpaySignature")

                        _paymentState.value = PaymentState.PaymentVerified

                    } else {
                        _paymentState.value = PaymentState.Error(response.message)
                    }
                }
        }
    }

    fun createOrderWithPayment(
        userId: String?,
        items: List<OrderItemDto>,
        totalPrice: Double,
        deliveryAddress: String,
        currency: String = "INR",
        notes: String? = null,
        shippingCharge:Double?
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
                notes = notes,
                razorpayOrderId = razorpayOrderId,
                razorpayPaymentId = razorpayPaymentId,
                razorpaySignature = razorpaySignature,
                shippingCharge = shippingCharge,
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
                        cartRepository.clearCart(userId ?: "")
                        if (notes == "COD") {
                            _paymentState.value = PaymentState.CodOrderCreated(response.order.toDomain())
                        } else {
                            _paymentState.value = PaymentState.PaymentSuccess
                        }
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