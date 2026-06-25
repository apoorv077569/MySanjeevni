package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.BookLabTestUseCase
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabBookingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookLabTestViewModel @Inject constructor(
    private val bookLabTestUseCase: BookLabTestUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LabBookingState())
    val state: StateFlow<LabBookingState> = _state.asStateFlow()

    fun bookLabTest(
        request: CreateLabBookingRequestDto
    ) {

        Log.d("LAB_BOOKING_VM", "bookLabTest() called")
        Log.d("LAB_BOOKING_VM", "Request = $request")

        viewModelScope.launch {

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