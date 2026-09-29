package com.mysanjeevni.mysanjeevni.features.category.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.category.presentation.state.CategoryState
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "CATEGORY_DEBUG"
    }

    var state by mutableStateOf(CategoryState())
        private set

    init {
        Log.d(TAG, "================================")
        Log.d(TAG, "CategoryViewModel CREATED")
        Log.d(TAG, "================================")

        loadCategories()
    }

    fun loadCategories() {

        Log.d(TAG, "========== LOAD CATEGORIES ==========")
        Log.d(TAG, "Starting GetCategoriesUseCase...")

        viewModelScope.launch {

            state = state.copy(
                isLoading = true,
                error = null
            )

            Log.d(TAG, "Loading = true")

            try {

                val result = getCategoriesUseCase()

                Log.d(TAG, "========== API RESULT ==========")
                Log.d(TAG, "Total categories received = ${result.size}")

                result.forEachIndexed { index, category ->

                    Log.d(
                        TAG,
                        "Category[$index] = $category"
                    )
                }

                Log.d(TAG, "========== SAVING STATE ==========")

                state = state.copy(
                    isLoading = false,
                    categories = result,
                    error = null
                )

                Log.d(
                    TAG,
                    "State categories count = ${state.categories.size}"
                )

                Log.d(
                    TAG,
                    "Loading = ${state.isLoading}"
                )

                Log.d(TAG, "========== CATEGORY LOAD SUCCESS ==========")

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "========== CATEGORY LOAD FAILED =========="
                )

                Log.e(
                    TAG,
                    "Error message = ${e.message}"
                )

                Log.e(
                    TAG,
                    "Error type = ${e::class.java.simpleName}"
                )

                Log.e(
                    TAG,
                    "Full error",
                    e
                )

                state = state.copy(
                    isLoading = false,
                    error = e.message
                )

                Log.d(
                    TAG,
                    "State error = ${state.error}"
                )
            }
        }
    }
}