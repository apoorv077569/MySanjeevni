package com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.AddToCartUseCase
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.FeaturedMedicine
import com.mysanjeevni.mysanjeevni.features.home.presentation.util.HomeMockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase
): ViewModel() {

    // --- 1. SEARCH STATE ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // --- 2. DATA SOURCE (Mock Data) ---
    // We load the mock data here so we can filter it
    private val _allMedicines = MutableStateFlow(HomeMockData.getMedicines())

    // --- 3. FILTERING LOGIC ---
    // This automatically runs whenever _searchQuery changes
    val searchResults = _searchQuery.combine(_allMedicines) { query, medicines ->
        if (query.isBlank()) {
            emptyList() // Return empty list if user is not searching
        } else {
            medicines.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- 4. FUNCTION TO UPDATE SEARCH TEXT ---
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // --- EXISTING CART LOGIC ---
    fun addToCart(medicine: FeaturedMedicine){
        viewModelScope.launch {
            val cleanPrice = medicine.price.replace("₹","").trim().toDoubleOrNull()?:0.0

            val cartItem = CartItem(
                medicine.id,
                medicine.name,
                cleanPrice,
                cleanPrice + 50,
                1,
                medicine.imageRes
            )
            addToCartUseCase(cartItem)
        }
    }
}