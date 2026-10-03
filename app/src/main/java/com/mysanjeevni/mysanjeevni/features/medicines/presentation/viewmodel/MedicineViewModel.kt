package com.mysanjeevni.mysanjeevni.features.medicines.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.currency.domain.usecase.GetCurrencyUseCase
import com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase.GetMedicineByIdUseCase
import com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase.GetMedicinesUseCase
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.state.MedicineState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getMedicinesUseCase: GetMedicinesUseCase,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase

) : ViewModel() {

    companion object {
        private const val TAG = "MEDICINE_PAGINATION"
        private const val PAGE_SIZE = 20
    }
    private val _state =
        MutableStateFlow(MedicineState())

    val state: StateFlow<MedicineState> =
        _state.asStateFlow()

    init {
        loadMedicines()
    }

    private fun loadMedicines() {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    error = null,
                    currentPage = 0,
                    hasMorePages = true
                )
            }
            val currencyResult = getCurrencyUseCase()
            if (currencyResult.isFailure) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = currencyResult.exceptionOrNull()?.message
                            ?: "Unable to detect currency"
                    )
                }
                return@launch
            }

            val currencyInfo = currencyResult.getOrThrow()

            getMedicinesUseCase(
                page = 1,
                limit = PAGE_SIZE
            )
                .onSuccess { medicines ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            medicines = medicines,
                            currencyInfo = currencyInfo
                        )
                    }
                }
                .onFailure { error ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun getMedicineById(
        id: String
    ) {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            getMedicineByIdUseCase(id)
                .onSuccess { medicine ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedMedicine = medicine
                        )
                    }
                }
                .onFailure { error ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun refresh() {
        loadMedicines()
    }
}