package com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase.CancelConsultationUseCase
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultationDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultationDetailViewModel @Inject constructor(
    private val cancelConsultationUseCase: CancelConsultationUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ConsultationDetailVM"
    }

    private val _state =
        MutableStateFlow(ConsultationDetailUiState())

    val state: StateFlow<ConsultationDetailUiState> =
        _state.asStateFlow()


    fun setConsultation(
        consultation: Consultation
    ) {

        Log.d(
            TAG,
            "Setting consultation: ${consultation.id}"
        )

        _state.update {
            it.copy(
                consultation = consultation,
                error = null
            )
        }
    }


    fun cancelConsultation() {

        val consultation =
            _state.value.consultation

        if (consultation == null) {

            Log.e(
                TAG,
                "Cannot cancel: consultation is null"
            )

            _state.update {
                it.copy(
                    error = "Consultation not found"
                )
            }

            return
        }

        if (
            consultation.status.equals(
                "completed",
                ignoreCase = true
            )
        ) {

            Log.e(
                TAG,
                "Cannot cancel completed consultation"
            )

            _state.update {
                it.copy(
                    error =
                        "Completed consultation cannot be cancelled"
                )
            }

            return
        }

        if (
            consultation.status.equals(
                "cancelled",
                ignoreCase = true
            )
        ) {

            Log.e(
                TAG,
                "Consultation already cancelled"
            )

            _state.update {
                it.copy(
                    error =
                        "Consultation already cancelled"
                )
            }

            return
        }


        viewModelScope.launch {

            _state.update {
                it.copy(
                    isCancelling = true,
                    error = null,
                    message = null
                )
            }

            Log.d(
                TAG,
                "Cancelling consultation: ${consultation.id}"
            )

            cancelConsultationUseCase(
                consultation.id
            )
                .onSuccess { response ->

                    Log.d(
                        TAG,
                        "Cancellation successful"
                    )

                    Log.d(
                        TAG,
                        "Message = ${response.message}"
                    )

                    Log.d(
                        TAG,
                        "Refund ID = ${response.refundId}"
                    )

                    _state.update {

                        it.copy(
                            consultation =
                                response.consultation
                                    ?: consultation.copy(
                                        status = "cancelled"
                                    ),

                            isCancelling = false,

                            isCancelled = true,

                            error = null,

                            message = response.message
                        )
                    }
                }
                .onFailure { exception ->

                    Log.e(
                        TAG,
                        "Cancellation failed",
                        exception
                    )

                    _state.update {

                        it.copy(
                            isCancelling = false,
                            isCancelled = false,
                            error =
                                exception.message
                                    ?: "Failed to cancel consultation"
                        )
                    }
                }
        }
    }
}