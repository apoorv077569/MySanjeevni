package com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase.GetMedicineByIdUseCase
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.orders.domain.usecase.CancelOrderUseCase
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderDetailState
import com.mysanjeevni.mysanjeevni.features.profile.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.AddressRepository
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val addressRepository: AddressRepository,
    private val sessionManager: SessionManager,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase

) : ViewModel() {

    var state by mutableStateOf(OrderDetailState())
        private set

    var deliveryFee by mutableDoubleStateOf(0.0)
        private set

    var isCheckingDelivery by mutableStateOf(false)
        private set

    var isCancelling by mutableStateOf(false)
        private set

    var cancelMessage by mutableStateOf<String?>(null)
        private set

    fun loadOrder(orderId: String) {

        viewModelScope.launch {

            try {

                state = state.copy(
                    isLoading = true,
                    error = null
                )

                val orderResult =
                    orderRepository.getRazorpayOrder(orderId)


                orderResult
                    .onSuccess { order ->

                        val token =
                            sessionManager.getToken().orEmpty()

                        val userId =
                            sessionManager.getUserId().orEmpty()

                        // -----------------------------
                        // LOAD ADDRESSES
                        // -----------------------------

                        val addressResponse =
                            addressRepository.fetchAddresses(
                                token = token,
                                userId = userId
                            )

                        val addresses =
                            if (addressResponse.isSuccessful) {

                                addressResponse.body()
                                    ?.addresses
                                    ?.map { dto ->
                                        dto.toDomain()
                                    }
                                    ?: emptyList()

                            } else {

                                emptyList()
                            }

                        val selectedAddress =
                            addresses.find { address ->
                                address.id == order.deliveryAddress
                            }

                        // -----------------------------
                        // LOAD MEDICINES
                        // -----------------------------

                        val medicines =
                            mutableListOf<Medicine>()
                        deliveryFee =
                            order.totalPrice -
                                    order.items.sumOf {
                                        it.price * it.quantity
                                    } -
                                    order.shippingCharge

                        Log.d(
                            "ORDER_PAYMENT",
                            "Original Price = ${
                                order.items.sumOf {
                                    it.price * it.quantity
                                }
                            }"
                        )

                        Log.d(
                            "ORDER_PAYMENT",
                            "Delivery Fee = ₹$deliveryFee"
                        )

                        Log.d(
                            "ORDER_PAYMENT",
                            "Platform Fee = ₹${order.shippingCharge}"
                        )

                        Log.d(
                            "ORDER_PAYMENT",
                            "Total = ₹${order.totalPrice}"
                        )

                        order.items.forEach { item ->

                            if (item.productId.isNotEmpty()) {

                                getMedicineByIdUseCase(
                                    item.productId
                                ).onSuccess { medicine ->

                                    medicines.add(medicine)
                                }
                            }
                        }

                        // -----------------------------
                        // FINAL STATE
                        // -----------------------------

                        state = state.copy(
                            isLoading = false,
                            order = order,
                            address = selectedAddress,
                            medicines = medicines,
                            error = null
                        )
                    }
                    .onFailure { exception ->

                        state = state.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to load order"
                        )
                    }

            } catch (e: Exception) {

                state = state.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Something went wrong"
                )
            }
        }
    }

    private fun checkDeliveryFee(pincode: String) {

        if (pincode.length != 6) {
            Log.d(
                "ORDER_DETAIL_SHIPPING",
                "Invalid pincode = $pincode"
            )
            return
        }

        viewModelScope.launch {

            try {

                Log.d(
                    "ORDER_DETAIL_SHIPPING",
                    "=============================="
                )

                Log.d(
                    "ORDER_DETAIL_SHIPPING",
                    "CHECKING SHIPROCKET DELIVERY"
                )

                Log.d(
                    "ORDER_DETAIL_SHIPPING",
                    "Pincode = $pincode"
                )

                isCheckingDelivery = true

                orderRepository
                    .checkServiceability(pincode)
                    .onSuccess { serviceability ->

                        Log.d(
                            "ORDER_DETAIL_SHIPPING",
                            "Serviceable = ${serviceability.serviceable}"
                        )

                        Log.d(
                            "ORDER_DETAIL_SHIPPING",
                            "Courier = ${serviceability.courierName}"
                        )

                        Log.d(
                            "ORDER_DETAIL_SHIPPING",
                            "Delivery Charge = ₹${serviceability.deliveryCharge}"
                        )

                        if (serviceability.serviceable) {

                            deliveryFee =
                                serviceability.deliveryCharge

                        } else {

                            deliveryFee = 0.0

                            Log.d(
                                "ORDER_DETAIL_SHIPPING",
                                "No delivery service available"
                            )
                        }

                        isCheckingDelivery = false
                    }
                    .onFailure { exception ->

                        Log.e(
                            "ORDER_DETAIL_SHIPPING",
                            "Serviceability failed",
                            exception
                        )

                        deliveryFee = 0.0
                        isCheckingDelivery = false
                    }

            } catch (e: Exception) {

                Log.e(
                    "ORDER_DETAIL_SHIPPING",
                    "Exception while checking delivery",
                    e
                )

                deliveryFee = 0.0
                isCheckingDelivery = false
            }
        }
    }

    fun cancelOrder(
        orderId: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            val userId =
                sessionManager.getUserId().orEmpty()

            try {

                state = state.copy(
                    isCancelling = true,
                    cancelMessage = null
                )

                Log.d(
                    "CANCEL_ORDER",
                    "Starting cancellation"
                )

                Log.d(
                    "CANCEL_ORDER",
                    "Order ID = $orderId"
                )

                Log.d(
                    "CANCEL_ORDER",
                    "User ID = $userId"
                )

                cancelOrderUseCase(
                    orderId = orderId,
                    userId = userId
                )
                    .onSuccess { response ->

                        Log.d(
                            "CANCEL_ORDER",
                            "Cancellation Success"
                        )

                        Log.d(
                            "CANCEL_ORDER",
                            "Message = ${response.message}"
                        )

                        state = state.copy(
                            isCancelling = false,
                            cancelMessage = response.message,
                            order = state.order?.copy(
                                status = "cancelled",
                                paymentStatus = response.order.paymentStatus
                            )
                        )


                        // FCM SUCCESS
                        try {

                            FcmHelper.sendNotification(
                                userId,
                                "Order Cancelled",
                                "Your order #${orderId.takeLast(8).uppercase()} has been cancelled successfully."
                            )

                            Log.d(
                                "CANCEL_ORDER",
                                "Success notification sent"
                            )

                        } catch (e: Exception) {

                            Log.e(
                                "CANCEL_ORDER",
                                "FCM success notification failed",
                                e
                            )
                        }

                        onSuccess()
                    }
                    .onFailure { exception ->

                        Log.e(
                            "CANCEL_ORDER",
                            "Cancellation Failed",
                            exception
                        )

                        state = state.copy(
                            isCancelling = false,
                            cancelMessage = exception.message
                                ?: "Failed to cancel order"
                        )

                        // FCM FAILURE
                        try {

                            FcmHelper.sendNotification(
                                userId,
                                "Cancellation Failed",
                                "We couldn't cancel your order: ${
                                    exception.localizedMessage
                                        ?: "Something went wrong"
                                }"
                            )

                            Log.d(
                                "CANCEL_ORDER",
                                "Failure notification sent"
                            )

                        } catch (e: Exception) {

                            Log.e(
                                "CANCEL_ORDER",
                                "FCM failure notification failed",
                                e
                            )
                        }
                    }

            } catch (e: Exception) {

                Log.e(
                    "CANCEL_ORDER",
                    "Cancellation Exception",
                    e
                )

                state = state.copy(
                    isCancelling = false,
                    cancelMessage = e.message
                        ?: "Failed to cancel order"
                )

                // FCM EXCEPTION
                try {

                    FcmHelper.sendNotification(
                        userId,
                        "Cancellation Failed",
                        "We couldn't cancel your order: ${
                            e.localizedMessage
                                ?: "Something went wrong"
                        }"
                    )

                    Log.d(
                        "CANCEL_ORDER",
                        "Exception notification sent"
                    )

                } catch (fcmException: Exception) {

                    Log.e(
                        "CANCEL_ORDER",
                        "FCM exception notification failed",
                        fcmException
                    )
                }
            }
        }
    }
}