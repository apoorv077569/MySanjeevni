package com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.currency.domain.usecase.GetCurrencyUseCase
import com.mysanjeevni.mysanjeevni.features.currency.presentation.state.CurrencyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        CurrencyState(isLoading = true)
    )

    val state: StateFlow<CurrencyState> =
        _state.asStateFlow()

    init {
        loadCurrency()
    }

    private fun loadCurrency() {
        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            getCurrencyUseCase()
                .onSuccess { currencyInfo ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            currencyInfo = currencyInfo,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            currencyInfo = null,
                            error = exception.message
                                ?: "Unable to load currency"
                        )
                    }
                }
        }
    }

    fun refresh() {
        loadCurrency()
    }
}