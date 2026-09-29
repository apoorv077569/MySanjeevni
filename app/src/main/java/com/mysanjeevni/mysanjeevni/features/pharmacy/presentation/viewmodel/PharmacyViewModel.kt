package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase.GetCategoriesUseCase
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase.GetMedicineUseCase
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.state.PharmacyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PharmacyViewModel @Inject constructor(
    private val getMedicineUseCase: GetMedicineUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "PHARMACY_PAGINATION"
        private const val PAGE_SIZE = 20
    }

    private val _state = MutableStateFlow(PharmacyState())
    val state: StateFlow<PharmacyState> = _state

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _userCity = MutableStateFlow("India")
    val userCity: StateFlow<String> = _userCity.asStateFlow()

    // Currently selected category
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        loadMedicines()
    }

    /**
     * Initial load
     */
    private fun loadMedicines() {

        viewModelScope.launch {

            _state.value = PharmacyState(
                isLoading = true
            )

            try {

                val medicineDeferred = async {
                    getMedicineUseCase(
                        page = 1,
                        limit = PAGE_SIZE,
                        category = _selectedCategory.value
                    )
                }

                val categoriesDeferred = async {
                    getCategoriesUseCase()
                }

                val medicineResult = medicineDeferred.await()
                val categoriesResult = categoriesDeferred.await()

                Log.d(
                    TAG,
                    "Items Received = ${medicineResult.medicines.size}"
                )

                Log.d(
                    TAG,
                    "Total Medicines = ${medicineResult.total}"
                )

                Log.d(
                    TAG,
                    "Total Pages = ${medicineResult.totalPages}"
                )

                Log.d(
                    TAG,
                    "Category = ${_selectedCategory.value}"
                )

                Log.d(
                    TAG,
                    "ALL CATEGORIES = ${categoriesResult.size}"
                )

                _state.value = PharmacyState(

                    medicines = medicineResult.medicines,

                    totalMedicines = medicineResult.total,

                    categories = categoriesResult,

                    isLoading = false,

                    isLoadingMore = false,

                    currentPage = 1,

                    hasMorePages =
                        1 < medicineResult.totalPages
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Pharmacy loading failed",
                    e
                )

                _state.value = PharmacyState(
                    isLoading = false,
                    error = e.message ?: "Unknown Error"
                )
            }
        }
    }

    /**
     * Called when user clicks category
     */
    fun onCategorySelected(category: String) {

        val newCategory =
            if (category == "All") {
                null
            } else {
                category
            }

        Log.d(
            TAG,
            "Category selected = $newCategory"
        )

        _selectedCategory.value = newCategory

        loadMedicinesForCategory()
    }

    /**
     * Load first page for selected category
     */
    private fun loadMedicinesForCategory() {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                medicines = emptyList(),
                isLoading = true,
                isLoadingMore = false,
                currentPage = 0,
                hasMorePages = true,
                error = ""
            )

            try {

                val category = _selectedCategory.value

                Log.d(
                    TAG,
                    "Loading category = $category"
                )

                val result = getMedicineUseCase(
                    page = 1,
                    limit = PAGE_SIZE,
                    category = category
                )

                Log.d(
                    TAG,
                    "Category medicines = ${result.medicines.size}"
                )

                Log.d(
                    TAG,
                    "Category total = ${result.total}"
                )

                Log.d(
                    TAG,
                    "Category total pages = ${result.totalPages}"
                )

                _state.value = _state.value.copy(

                    medicines = result.medicines,

                    totalMedicines = result.total,

                    isLoading = false,

                    isLoadingMore = false,

                    currentPage = 1,

                    hasMorePages =
                        1 < result.totalPages,

                    error = ""
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Category loading failed",
                    e
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = e.message ?: "Failed to load medicines"
                )
            }
        }
    }

    /**
     * Load next page
     */
    fun loadNextPage() {

        val currentState = _state.value

        if (currentState.isLoading) {
            Log.d(
                TAG,
                "Skip: first page loading"
            )
            return
        }

        if (currentState.isLoadingMore) {
            Log.d(
                TAG,
                "Skip: next page already loading"
            )
            return
        }

        if (!currentState.hasMorePages) {
            Log.d(
                TAG,
                "Skip: no more pages"
            )
            return
        }

        val nextPage =
            currentState.currentPage + 1

        val category =
            _selectedCategory.value

        Log.d(
            TAG,
            "Loading next page = $nextPage"
        )

        Log.d(
            TAG,
            "Category = $category"
        )

        viewModelScope.launch {

            _state.value = currentState.copy(
                isLoadingMore = true,
                error = ""
            )

            try {

                val result = getMedicineUseCase(
                    page = nextPage,
                    limit = PAGE_SIZE,
                    category = category
                )

                Log.d(
                    TAG,
                    "Page $nextPage loaded"
                )

                Log.d(
                    TAG,
                    "Items Received = ${result.medicines.size}"
                )

                Log.d(
                    TAG,
                    "Total Medicines = ${result.total}"
                )

                Log.d(
                    TAG,
                    "Total Pages = ${result.totalPages}"
                )

                val updatedMedicines =
                    currentState.medicines + result.medicines

                _state.value = currentState.copy(

                    medicines = updatedMedicines,

                    totalMedicines = result.total,

                    isLoadingMore = false,

                    currentPage = nextPage,

                    hasMorePages =
                        nextPage < result.totalPages,

                    error = ""
                )

                Log.d(
                    TAG,
                    "Total loaded now = ${updatedMedicines.size}"
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Page $nextPage failed",
                    e
                )

                _state.value = currentState.copy(
                    isLoadingMore = false,
                    error = e.message ?: "Failed to load more"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}