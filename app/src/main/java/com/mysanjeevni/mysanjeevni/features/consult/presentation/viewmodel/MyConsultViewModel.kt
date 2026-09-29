package com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.consult.data.mapper.toConsultItem
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase.GetConsultationUseCase
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyConsultViewModel @Inject constructor(
    private val getConsultationsUseCase: GetConsultationUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    companion object {
        private const val TAG = "MyConsultViewModel"
    }

    private val _state = MutableStateFlow(
        ConsultState()
    )

    val state: StateFlow<ConsultState> =
        _state.asStateFlow()

    init {
        loadConsults()
    }

    fun loadConsults() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            val userId = sessionManager.getUserId()

            Log.d(
                TAG,
                "Session userId=$userId"
            )

            if (userId.isNullOrBlank()) {

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "User session not found"
                    )
                }

                return@launch
            }

            getConsultationsUseCase(userId)
                .onSuccess { consultations ->

                    val upcoming = consultations
                        .filter {
                            it.status.equals("pending", ignoreCase = true) ||
                                    it.status.equals("confirmed", ignoreCase = true)
                        }
                        .map { it.toConsultItem() }

                    val past = consultations
                        .filter {
                            !(
                                    it.status.equals("pending", ignoreCase = true) ||
                                            it.status.equals("confirmed", ignoreCase = true)
                                    )
                        }
                        .map { it.toConsultItem() }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            consultations = consultations,
                            upcomingConsults = upcoming,
                            pastConsults = past,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to load consultations"
                        )
                    }
                }
        }
    }
    fun getConsultationById(consultationId: String): Consultation? {
        val consultation = _state.value.consultations.find { it.id == consultationId }
        _state.update {
            it.copy(
                selectedConsultation = consultation
            )
        }
        return consultation
    }
    fun onTabSelected(index: Int) {

        _state.update {
            it.copy(
                selectedTab = index
            )
        }
    }

    fun retry() {
        loadConsults()
    }
}