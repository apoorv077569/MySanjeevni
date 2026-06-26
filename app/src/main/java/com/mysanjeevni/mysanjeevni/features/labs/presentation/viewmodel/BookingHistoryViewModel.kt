package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import androidx.compose.material3.FilterChip
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.BookingHistoryRepository
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.CancelBookingUseCase
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.SyncBookingUseCase
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.BookingHistoryState
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingHistoryViewModel @Inject constructor(
    private val repository: BookingHistoryRepository,
    private val cancelBookingUseCase: CancelBookingUseCase,
    private val syncBookingUseCase: SyncBookingUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(
        BookingHistoryState()
    )

    val state = _state.asStateFlow()


    init {
        getBookingHistory()
    }

    fun getBookingHistory() {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            repository.getBookingHistory()
                .onSuccess { bookings ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            bookings = bookings
                        )
                    }
                }
                .onFailure { e ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
        }
    }

    fun cancelBooking(bookingId: String) {
        val userId = sessionManager.getUserId()
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isCancelling = true
                )
            }

            cancelBookingUseCase(
                bookingId
            ).onSuccess {

                _state.update {
                    it.copy(
                        isCancelling = false,
                        cancelSuccess = true
                    )
                }
                FcmHelper.sendNotification(
                    userId.toString(),
                    "Booking Cancelled",
                    "Your booking for lab test has been cancelled"
                )

                getBookingHistory()
            }
                .onFailure { e ->

                    _state.update {
                        it.copy(
                            isCancelling = false,
                            error = e.message
                        )
                    }
                    FcmHelper.sendNotification(
                        userId.toString(),
                        "Cancellation Failed",
                        "We couldn't cancel your booking: ${e.localizedMessage}"
                    )
                }
        }
    }

    fun getPatientName(): String {
        return sessionManager.getUserName() ?: ""
    }

    fun getPatientPhone(): String {
        return sessionManager.getPhone() ?: ""
    }

    fun syncBooking(
        bookingId: String
    ) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            _state.update {
                it.copy(
                    isSyncing = true
                )
            }
            syncBookingUseCase(
                bookingId
            ).onSuccess {
                _state.update {
                    it.copy(
                        isSyncing = false
                    )
                }
                FcmHelper.sendNotification(
                    userId.toString(),
                    "Booking Synced",
                    "Your booking information has been updated successfully"
                )
                getBookingHistory()
            }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isSyncing = false,
                            error = e.message
                        )
                    }
                    FcmHelper.sendNotification(
                        userId.toString(),
                        "Sync Failed",
                        "Could not sync booking: ${e.message}"
                    )
                }
        }
    }
}