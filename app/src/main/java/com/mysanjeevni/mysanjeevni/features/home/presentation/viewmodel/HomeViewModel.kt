package com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.home.domain.repository.LocationRepository
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val api: ApiService,

    ) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _dealTimeLeft = MutableStateFlow(36000L)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    private val _popularProducts = MutableStateFlow<List<Medicine>>(emptyList())
    val popularProducts: StateFlow<List<Medicine>> =
        _popularProducts.asStateFlow()

    private val _allMedicines =
        MutableStateFlow<List<Medicine>>(emptyList())

    val allMedicines: StateFlow<List<Medicine>> = _allMedicines
    val userCity = locationRepository.city
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    companion object{
        private const val PAGE_SIZE = 20
    }

    init {
        loadHomeData()
        loadPopularProducts()
        startDealTimer()

    }

    private fun startDealTimer() {
        viewModelScope.launch {
            while (_dealTimeLeft.value > 0) {
                delay(1000.milliseconds)
                _dealTimeLeft.value--
            }
        }
    }

    fun fetchCurrentCity() {
        viewModelScope.launch {
            locationRepository.fetchCurrentCity()
        }
    }

    fun refresh(){
        viewModelScope.launch {
            _isRefreshing.value = true
            loadHomeData()
            loadPopularProducts()
            _isRefreshing.value = false
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {

            _isLoading.value = true

            try {
                Log.d(
                    "HOME_PAGINATION",
                    "Starting medicine pagination"
                )

                val allProducts = mutableListOf<Medicine>()

                var currentPage = 1
                var hasMorePages = true

                while (hasMorePages) {

                    Log.d(
                        "HOME_PAGINATION",
                        "Calling page=$currentPage, limit=$PAGE_SIZE"
                    )

                    val res = api.getMedicines(
                        page = currentPage,
                        limit = PAGE_SIZE
                    )

                    Log.d(
                        "HOME_PAGINATION",
                        "Response code=${res.code()}"
                    )

                    if (!res.isSuccessful) {

                        val errorBody =
                            res.errorBody()?.string()

                        Log.e(
                            "HOME_PAGINATION",
                            "API failed: $errorBody"
                        )

                        break
                    }

                    val products =
                        res.body()?.products.orEmpty()

                    Log.d(
                        "HOME_PAGINATION",
                        "Page $currentPage received=${products.size}"
                    )

                    val mappedMedicines =
                        products.map { dto ->

                            Medicine(
                                id = dto._id,
                                name = dto.name,
                                description = dto.description.orEmpty(),
                                price = dto.price,
                                mrp = dto.mrp,
                                category = dto.category,
                                diseaseCategory =
                                    dto.diseaseCategory.orEmpty(),
                                diseaseSubcategory =
                                    dto.diseaseSubcategory.orEmpty(),
                                productType = dto.productType,
                                brand = dto.brand.orEmpty(),
                                stock = dto.stock,
                                quantity = dto.quantity,
                                quantityUnit = dto.quantityUnit,
                                image = dto.image.orEmpty(),
                                images = dto.images.orEmpty(),
                                specifications =
                                    dto.specifications.orEmpty(),
                                safetyInformation =
                                    dto.safetyInformation.orEmpty(),
                                requiresPrescription =
                                    dto.requiresPrescription,
                                vendorName =
                                    dto.vendorName.orEmpty(),
                                vendorRating =
                                    dto.vendorRating ?: 0.0,
                                rating = dto.rating,
                                reviews = dto.reviews,
                                icon = dto.icon
                            )
                        }

                    allProducts.addAll(mappedMedicines)

                    Log.d(
                        "HOME_PAGINATION",
                        "Total loaded=${allProducts.size}"
                    )

                    if (products.size < PAGE_SIZE) {

                        Log.d(
                            "HOME_PAGINATION",
                            "Last page reached"
                        )

                        hasMorePages = false

                    } else {

                        currentPage++
                    }
                }

                _allMedicines.value =
                    allProducts.distinctBy { it.id }

                Log.d(
                    "HOME_PAGINATION",
                    "Final medicines count=${_allMedicines.value.size}"
                )

            } catch (e: Exception) {

                Log.e(
                    "HOME_PAGINATION",
                    "Exception=${e.localizedMessage}",
                    e
                )

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
                            id = dto._id,
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
                            reviews = dto.reviews,
                            icon  = dto.icon
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
        locationRepository.updateCity(city)
    }
}