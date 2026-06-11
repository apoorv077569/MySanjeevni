package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.PaginationDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.usecase.GetLabTestsUseCase
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabTestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabTestViewModel @Inject constructor(
    private val getLabTestsUseCase: GetLabTestsUseCase
) : ViewModel() {

    val labTests = getLabTestsUseCase()
        .cachedIn(viewModelScope)
}