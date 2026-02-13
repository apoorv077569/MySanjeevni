package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase.GetMedicineUseCase
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.state.PharamcyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PharmacyViewModel @Inject constructor(
    private val getMedicineUseCase: GetMedicineUseCase
): ViewModel(){
    private val _state = MutableStateFlow(PharamcyState())
    val state: StateFlow<PharamcyState> =_state

    init {
        loadMedicines()
    }

    private fun loadMedicines() {
        viewModelScope.launch {
            // show loading
            _state.value = PharamcyState(isLoading = true)
            try{
                // fetch data
                val result = getMedicineUseCase()

                // show success
                _state.value = PharamcyState(medicines = result)
            }catch (e: Exception){
                // show error
                _state.value  = PharamcyState(error = e.message?:"Unknown Error")
            }
        }
    }
}