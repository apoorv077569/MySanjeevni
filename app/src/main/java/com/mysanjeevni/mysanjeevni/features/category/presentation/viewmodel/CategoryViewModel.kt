package com.mysanjeevni.mysanjeevni.features.category.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.category.domain.usecase.GetCategoriesUseCase
import com.mysanjeevni.mysanjeevni.features.category.presentation.state.CategoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    var state by mutableStateOf(CategoryState())
        private set

    init {
        loadCategories()
    }

    fun loadCategories() {

        viewModelScope.launch {

            state = state.copy(isLoading = true)

            try {

                val result = getCategoriesUseCase()

                state = state.copy(
                    isLoading = false,
                    categories = result
                )

            } catch (e: Exception) {

                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}