package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase.GetMedicineUseCase
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.state.PharmacyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PharmacyViewModel @Inject constructor(private val getMedicineUseCase: GetMedicineUseCase): ViewModel(){

    companion object{
        private const val TAG = "PHARMACY_PAGINATION"
        private const val PAGE_SIZE = 20
    }

    private val _state = MutableStateFlow(PharmacyState())
    val state: StateFlow<PharmacyState> =_state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _userCity = MutableStateFlow("India")
    val userCity: StateFlow<String> = _userCity.asStateFlow()

    init {
        loadMedicines()
    }


    private fun loadMedicines() {
        viewModelScope.launch {
            // show loading
            Log.d(TAG,"Loading first page")
            _state.value = PharmacyState(isLoading = true)
            try{
                val firstPage = 1
                // fetch data
                val result = getMedicineUseCase(page = firstPage,limit = PAGE_SIZE)

                Log.d(TAG,"Page $firstPage loaded")
                Log.d(TAG,"Items Received = ${result.size}")


                // show success
                _state.value = PharmacyState(
                    medicines = result,
                    isLoading = false,
                    isLoadingMore = false,
                    currentPage = firstPage,
                    hasMorePages = result.size == PAGE_SIZE
                    )
            }catch (e: Exception){
                Log.e(TAG,"First Page failed: ${e.message}")
                // show error
                _state.value  = PharmacyState(error = e.message?:"Unknown Error")
            }
        }
    }

    fun loadNextPage(){
        val currentState = _state.value

        if (currentState.isLoading){
            Log.d(TAG,"Skip: first page loading")
            return
        }
        if(currentState.isLoadingMore){
            Log.d(TAG,"Skip: next page already loading")
            return
        }
        if(!currentState.hasMorePages){
            Log.d(TAG,"Skip: no more pages")
            return
        }
        val nextPage = currentState.currentPage+1
        Log.d(TAG,"Loading next page = $nextPage")
        viewModelScope.launch {
            _state.value = currentState.copy(
                isLoadingMore = true,
                error = ""
            )
            try{
                val result = getMedicineUseCase(page = nextPage,limit = PAGE_SIZE)
                Log.d(TAG,"Page $nextPage loaded")
                Log.d(TAG,"Items Received ${result.size}")

                val updatedmedicines = currentState.medicines + result

                _state.value = currentState.copy(
                    medicines = updatedmedicines,
                    isLoadingMore = false,
                    currentPage = nextPage,
                    hasMorePages = result.size == PAGE_SIZE,
                    error = ""
                )
                Log.d(TAG,"Total medicines now = ${updatedmedicines.size}")

            }catch (e: Exception){
                Log.e(TAG,"Page $nextPage failed: ${e.message}")
                _state.value = currentState.copy(
                    isLoadingMore =  false,
                    error = e.message?:"Failed to load more"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}