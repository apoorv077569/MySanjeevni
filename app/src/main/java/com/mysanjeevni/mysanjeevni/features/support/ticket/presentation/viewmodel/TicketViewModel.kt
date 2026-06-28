package com.mysanjeevni.mysanjeevni.features.support.ticket.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.remote.client.AuthApiClient
import com.mysanjeevni.mysanjeevni.data.remote.model.notification.SendNotificationRequest
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.usecase.GetTicketsUseCase
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.usecase.RaiseTicketUseCase
import com.mysanjeevni.mysanjeevni.features.support.ticket.presentation.state.TicketUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TicketViewModel @Inject constructor(
    private val raiseTicketUseCase: RaiseTicketUseCase,
    private val getTicketsUseCase: GetTicketsUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "TICKET_VM"
    }

    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState: StateFlow<TicketUiState> = _uiState

    fun raiseTicket(
        userId: String,
        userName: String,
        email: String,
        role: String,
        category: String,
        subject: String,
        message: String
    ) {
        viewModelScope.launch {
            Log.d(TAG, "========== RAISE TICKET ==========")
            Log.d(TAG, "userId=$userId")
            Log.d(TAG, "userName=$userName")
            Log.d(TAG, "email=$email")
            Log.d(TAG, "role=$role")
            Log.d(TAG, "category=$category")
            Log.d(TAG, "subject=$subject")
            Log.d(TAG, "message=$message")
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            val result = raiseTicketUseCase(
                userId = userId,
                userName = userName,
                email = email,
                role = role,
                category = category,
                subject = subject,
                message = message
            )
            result.fold(
                onSuccess = { response ->
                    Log.d(TAG, "Ticket Created Successfully")
                    Log.d(TAG, "Message = ${response.message}")
                    CoroutineScope(Dispatchers.IO).launch {
                        try{
                            AuthApiClient.api.sendNotification(SendNotificationRequest(
                                userId = userId,
                                title = "Ticket Raised",
                                body = "Your ticket has been raised successfully."
                            ))
                            Log.d(TAG,"Notification sent Successfull")
                        }catch (e: Exception){
                            Log.e(TAG, "Failed to send notification", e)
                        }
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = response.message
                        )
                    }
                },
                onFailure = { error ->
                    Log.e(TAG, "Ticket Creation Failed")
                    Log.e(TAG, error.message ?: "Unknown Error")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    fun getTickets(userId: String) {
        viewModelScope.launch {
            Log.d(TAG, "========== GET TICKETS ==========")
            Log.d(TAG, "userId=$userId")
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            val result = getTicketsUseCase(userId)

            result.fold(
                onSuccess = { tickets ->

                    Log.d(TAG, "Tickets Loaded")
                    Log.d(TAG, "Count = ${tickets.size}")
                    Log.d(TAG, "Status = ${tickets.size}")

                    tickets.forEach {
                        Log.d(TAG, "Ticket = ${it.subject}")
                        Log.d(TAG, "Status = ${it.status}")
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tickets = tickets
                        )
                    }
                },
                onFailure = { error ->

                    Log.e(TAG, "Failed To Load Tickets")
                    Log.e(TAG, error.message ?: "Unknown Error")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null
            )
        }
    }
}