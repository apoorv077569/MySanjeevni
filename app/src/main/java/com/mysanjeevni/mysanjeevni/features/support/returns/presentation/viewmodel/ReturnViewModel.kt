package com.mysanjeevni.mysanjeevni.features.support.returns.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnRequestDto
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.usecase.GetReturnsUseCase
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.usecase.SubmitReturnUseCase
import com.mysanjeevni.mysanjeevni.features.support.returns.presentation.state.ReturnUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReturnViewModel @Inject constructor(
    private val submitReturnUseCase: SubmitReturnUseCase,
    private val getReturnsUseCase: GetReturnsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ReturnUiState())

    val uiState = _uiState.asStateFlow()

    fun getReturns(userId: String) {

        viewModelScope.launch {

            getReturnsUseCase(userId)
                .onSuccess {

                    _uiState.update { state ->
                        state.copy(
                            returns = it
                        )
                    }

                }
        }
    }

    fun submitReturn(
        request: ReturnRequestDto
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            submitReturnUseCase(request)
                .onSuccess { message ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = message
                        )
                    }

                    getReturns(request.userId)
                }
                .onFailure {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = it.errorMessage
                        )
                    }
                }
        }
    }
}