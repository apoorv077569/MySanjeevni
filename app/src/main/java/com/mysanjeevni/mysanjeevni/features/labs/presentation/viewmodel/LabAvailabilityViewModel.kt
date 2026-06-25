package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsRequestParams
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.CheckServiceabilityUseCase
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.SearchSlotsUseCase
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabAvailabilityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabAvailabilityViewModel @Inject constructor(
    private val checkServiceabilityUseCase: CheckServiceabilityUseCase,
    private val searchSlotsUseCase: SearchSlotsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LabAvailabilityState())
    val state: StateFlow<LabAvailabilityState> = _state.asStateFlow()

    private var serviceabilityJob: Job? = null

    fun onPincodeChanged(testId: String, pincode: String) {
        serviceabilityJob?.cancel()

        if (pincode.length != 6) {
            _state.update {
                it.copy(
                    serviceability = null,
                    serviceabilityError = null,
                    isCheckingServiceability = false
                )
            }
            return
        }

        serviceabilityJob = viewModelScope.launch {
            _state.update {
                it.copy(isCheckingServiceability = true, serviceabilityError = null)
            }

            delay(300) // debounce so it doesn't fire on every keystroke

            checkServiceabilityUseCase(testId, pincode)
                .onSuccess { result ->
                    Log.d("LAB_AVAILABILITY_VM", "Serviceability = $result")
                    _state.update {
                        it.copy(
                            isCheckingServiceability = false,
                            serviceability = result,
                            serviceabilityError = null
                        )
                    }
                }
                .onFailure { exception ->
                    Log.e("LAB_AVAILABILITY_VM", "Error = ${exception.message}")
                    _state.update {
                        it.copy(
                            isCheckingServiceability = false,
                            serviceability = null,
                            serviceabilityError = exception.message ?: "Unable to check serviceability"
                        )
                    }
                }
        }
    }

    fun searchSlots(params: SlotsRequestParams) {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingSlots = true, slotsError = null) }

            searchSlotsUseCase(params)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoadingSlots = false,
                            slotsResult = result
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoadingSlots = false,
                            slotsError = exception.message ?: "Unable to fetch slots"
                        )
                    }
                }
        }
    }

    fun selectSlot(slotId: String) {
        _state.update { it.copy(selectedSlotId = slotId) }
    }

    fun resetServiceability() {
        serviceabilityJob?.cancel()
        _state.update {
            it.copy(
                isCheckingServiceability = false,
                serviceability = null,
                serviceabilityError = null
            )
        }
    }
}