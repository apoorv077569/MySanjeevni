package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.GetLabTestDetailUseCase
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabDetailsViewModel @Inject constructor(
    private val useCase: GetLabTestDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LabDetailState())
    val state = _state.asStateFlow()

    init {

        val id = savedStateHandle.get<String>("id")

        if (!id.isNullOrBlank()) {
            load(id)
        }
    }

    private fun load(id: String) {

        Log.d("LAB_DETAIL", "Loading ID = $id")

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            useCase(id)
                .onSuccess { test ->

                    Log.d(
                        "LAB_DETAIL",
                        "Success = ${test.name}"
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            labTestDetail = test
                        )
                    }
                }
                .onFailure { e ->

                    Log.e(
                        "LAB_DETAIL",
                        "Failed",
                        e
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: ""
                        )
                    }
                }
        }
    }}