package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.BookLabTestUseCase
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabBookingState
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookLabTestViewModel @Inject constructor(
    private val bookLabTestUseCase: BookLabTestUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(LabBookingState())
    val state: StateFlow<LabBookingState> = _state.asStateFlow()
    private var pendingRequest: CreateLabBookingRequestDto? = null


    fun setPendingRequest(request: CreateLabBookingRequestDto) {

        Log.d("LAB_PENDING", "setPendingRequest()")
        Log.d("LAB_PENDING", "Request = $request")

        pendingRequest = request
    }

    fun bookPendingLabTest(
        paymentId: String,
        orderId: String,
        signature: String
    ) {

        Log.d("LAB_PENDING", "bookPendingLabTest() called")
        Log.d("LAB_PENDING", "Pending Request = $pendingRequest")

        if (pendingRequest == null) {
            Log.e("LAB_PENDING", "Pending request is NULL")
            return
        }

        val finalRequest = pendingRequest!!.copy(
            razorpayPaymentId = paymentId,
            razorpayOrderId = orderId,
            razorpaySignature = signature
        )

        Log.d("LAB_PENDING", "Final Request = $finalRequest")

        bookLabTest(finalRequest)
    }
    fun bookLabTest(
        request: CreateLabBookingRequestDto
    ) {

        Log.d("LAB_BOOKING_VM", "bookLabTest() called")
        Log.d("LAB_BOOKING_VM", "Request = $request")

        viewModelScope.launch {
            val userId = sessionManager.getUserId()

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null
                )
            }

            try {
                Log.d("LAB_BOOKING_VM", "Calling UseCase")
                bookLabTestUseCase(request)
                    .onSuccess { booking ->

                        Log.d(
                            "LAB_BOOKING_VM",
                            "Success = $booking"
                        )

                        _state.update {
                            it.copy(
                                isLoading = false,
                                booking = booking,
                                successMessage = "Lab Test Booked Successfully"
                            )
                        }
                        FcmHelper.sendNotification(
                            userId.toString(),
                            "Lab Test Booked",
                            "Your booking for ${request.testName} is confirmed"
                        )
                    }
                    .onFailure { exception ->

                        Log.e(
                            "LAB_BOOKING_VM",
                            "Failure = ${exception.message}",
                            exception
                        )

                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Booking failed"
                            )
                        }
                        FcmHelper.sendNotification(
                            userId.toString(),
                            "Error",
                            "A technical error occurred while booking your lab test"
                        )
                    }

            } catch (e: Exception) {
                Log.e(
                    "LAB_BOOKING_VM",
                    "Exception = ${e.message}",
                    e
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun clearMessage() {
        _state.update {
            it.copy(
                successMessage = null,
                error = null
            )
        }
    }
}