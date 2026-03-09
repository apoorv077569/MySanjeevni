package com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ConsultViewModel @Inject constructor() : ViewModel() {

    // 1. Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. Mock Data (Moved from UI to here)
    private val _allDoctors = MutableStateFlow(
        listOf(
            Doctor(1, "Dr. Anjali Gupta", "Dermatologist", "8 years exp", 4.8, 120, 499.0, "15 mins", "https://img.freepik.com/free-photo/pleased-young-female-doctor-wearing-medical-robe-stethoscope-around-neck-standing-with-closed-posture_409827-254.jpg"),
            Doctor(2, "Dr. Rajesh Kumar", "General Physician", "12 years exp", 4.5, 340, 399.0, "30 mins", "https://img.freepik.com/free-photo/doctor-with-his-arms-crossed-white-background_1368-5790.jpg"),
            Doctor(3, "Dr. Sneha Roy", "Gynecologist", "15 years exp", 4.9, 500, 699.0, "Available Now", "https://img.freepik.com/free-photo/woman-doctor-wearing-lab-coat-with-stethoscope-isolated_1303-29791.jpg"),
            Doctor(4, "Dr. Vikram Singh", "Dentist", "6 years exp", 4.4, 80, 299.0, "1 hour", "https://img.freepik.com/free-photo/portrait-smiling-handsome-male-doctor-man_171337-5055.jpg")
        )
    )

    // 3. Filter Logic (Search by Name OR Specialty)
    val searchResults = _searchQuery.combine(_allDoctors) { query, doctors ->
        if (query.isBlank()) {
            emptyList()
        } else {
            doctors.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.specialty.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 4. Update Function
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}