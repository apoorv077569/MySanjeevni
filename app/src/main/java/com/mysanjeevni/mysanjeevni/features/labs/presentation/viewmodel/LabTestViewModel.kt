package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.GetLabTestsUseCase
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LabTestViewModel @Inject constructor(
    private val getLabTestsUseCase: GetLabTestsUseCase
) : ViewModel() {

    private val _filter = MutableStateFlow(LabFilterState())
    val filter: StateFlow<LabFilterState> = _filter.asStateFlow()

    private val rawLabTests = getLabTestsUseCase().cachedIn(viewModelScope)

    // Re-emits whenever filter changes
    val labTests: Flow<PagingData<LabTest>> = _filter
        .flatMapLatest { f ->
            rawLabTests.map { pagingData ->
                pagingData.filter { test ->
                    val categoryMatch = f.category == "All" ||
                            test.category.equals(f.category, ignoreCase = true)
                    val priceMatch = test.price <= f.maxPrice.toInt()
                    categoryMatch && priceMatch
                }
            }
        }
        .cachedIn(viewModelScope)

    fun applyFilter(newFilter: LabFilterState) {
        _filter.value = newFilter
    }
}