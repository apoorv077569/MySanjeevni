package com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.AddToCartUseCase
import com.mysanjeevni.mysanjeevni.features.home.data.repository.LocationRepository
import com.mysanjeevni.mysanjeevni.features.home.model.FeaturedMedicine
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.medicines.data.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val addToCartUseCase: AddToCartUseCase,
    private val api: ApiService,

    ) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _dealTimeLeft = MutableStateFlow(36000L)
    val dealTimeLeft = _dealTimeLeft.asStateFlow()
    private val _popularProducts = MutableStateFlow<List<Medicine>>(emptyList())
    val popularProducts: StateFlow<List<Medicine>> =
        _popularProducts.asStateFlow()

    private val _allMedicines =
        MutableStateFlow<List<Medicine>>(emptyList())
    private val _userCity = MutableStateFlow("India")
    val userCity: StateFlow<String> = _userCity.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        loadHomeData()
        loadPopularProducts()
        startDealTimer()

    }

    private fun startDealTimer() {
        viewModelScope.launch {
            while (_dealTimeLeft.value > 0) {
                delay(1000)
                _dealTimeLeft.value--
            }
        }
    }

    fun fetchCurrentCity() {
        viewModelScope.launch {
            try {
                val city = locationRepository.getCurrentCity()
                _userCity.value = city
            } catch (e: Exception) {
                _userCity.value = "India"
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("HOME_DEBUG", "Calling getMedicines API...")
                val res = api.getMedicines()
                Log.d("HOME_DEBUG", "Response code: ${res.code()}")
                Log.d("HOME_DEBUG", "Response body: ${res.body()}")
                Log.d("HOME_DEBUG", "Error body: ${res.errorBody()?.string()}")

                if (res.isSuccessful) {
                    val data = res.body()?.products ?: emptyList()
                    Log.d("HOME_DEBUG", "Products count: ${data.size}")

                    _allMedicines.value = data.map { dto ->
                        Medicine(
                            id = dto._id.toString(),
                            name = dto.name,
                            description = dto.description.orEmpty(),
                            price = dto.price,
                            mrp = dto.mrp,
                            category = dto.category,
                            diseaseCategory = dto.diseaseCategory.orEmpty(),
                            diseaseSubcategory = dto.diseaseSubcategory.orEmpty(),
                            productType = dto.productType,
                            brand = dto.brand.orEmpty(),
                            stock = dto.stock,
                            quantity = dto.quantity,
                            quantityUnit = dto.quantityUnit,
                            image = dto.image.orEmpty(),
                            images = dto.images.orEmpty(),
                            specifications = dto.specifications.orEmpty(),
                            safetyInformation = dto.safetyInformation.orEmpty(),
                            requiresPrescription = dto.requiresPrescription,
                            vendorName = dto.vendorName.orEmpty(),
                            vendorRating = dto.vendorRating ?: 0.0,
                            rating = dto.rating,
                            reviews = dto.reviews
                        )
                    }
                    Log.d("HOME_DEBUG", "Medicines set: ${_allMedicines.value.size}")
                } else {
                    Log.e("HOME_DEBUG", "API Failed: ${res.code()} - ${res.message()}")
                }
            } catch (e: Exception) {
                Log.e("HOME_DEBUG", "Exception: ${e.localizedMessage}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun loadPopularProducts() {
        viewModelScope.launch {
            try {
                val res = api.getPopularProducts()
                Log.d("HOME_DEBUG", "Popular code: ${res.code()}")
                if (res.isSuccessful) {
                    val data = res.body()?.products ?: emptyList()
                    _popularProducts.value = data.map { dto ->
                        Medicine(
                            id = dto._id.toString(),
                            name = dto.name,
                            description = dto.description.orEmpty(),
                            price = dto.price,
                            mrp = dto.mrp,
                            category = dto.category,
                            diseaseCategory = dto.diseaseCategory.orEmpty(),
                            diseaseSubcategory = dto.diseaseSubcategory.orEmpty(),
                            productType = dto.productType,
                            brand = dto.brand.orEmpty(),
                            stock = dto.stock,
                            quantity = dto.quantity,
                            quantityUnit = dto.quantityUnit,
                            image = dto.image.orEmpty(),
                            images = dto.images.orEmpty(),
                            specifications = dto.specifications.orEmpty(),
                            safetyInformation = dto.safetyInformation.orEmpty(),
                            requiresPrescription = dto.requiresPrescription,
                            vendorName = dto.vendorName.orEmpty(),
                            vendorRating = dto.vendorRating ?: 0.0,
                            rating = dto.rating,
                            reviews = dto.reviews
                        )
                    }
                    Log.d("HOME_DEBUG", "Popular loaded: ${data.size}")
                }
            } catch (e: Exception) {
                Log.e("HOME_DEBUG", "Popular Error: ${e.localizedMessage}")
            }
        }
    }

    // --- 3. FILTERING LOGIC ---
    val searchResults = _searchQuery.combine(_allMedicines) { query, medicines ->
        if (query.isBlank()) {
            emptyList()
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

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }


    fun updateCity(city: String) {
        _userCity.value = city
    }

    fun refresh() {
        loadHomeData()
    }


}