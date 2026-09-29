package com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase.GetDoctorUseCase
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.DoctorListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultViewModel @Inject constructor(
    private val getDoctorsUseCase: GetDoctorUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ConsultViewModel"
    }

    private val _doctorState = MutableStateFlow(DoctorListState())
    val doctorState = _doctorState.asStateFlow()

    // ---------------------------------------------------------
    // FILTER STATE
    // ---------------------------------------------------------

    private val _selectedDepartment = MutableStateFlow<String?>(null)
    val selectedDepartment = _selectedDepartment.asStateFlow()

    private val _selectedSpecialization = MutableStateFlow<String?>(null)
    val selectedSpecialization = _selectedSpecialization.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // ---------------------------------------------------------
    // FILTER OPTIONS
    // ---------------------------------------------------------

    val departments = doctorState
        .combine(doctorState) { state, _ ->
            state.doctors
                .map { it.department }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val specializations = doctorState
        .combine(doctorState) { state, _ ->
            state.doctors
                .map { it.specialization }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // ---------------------------------------------------------
    // SEARCH + DEPARTMENT + SPECIALIZATION
    // ---------------------------------------------------------

    val searchResults = combine(
        _searchQuery,
        _selectedDepartment,
        _selectedSpecialization,
        _doctorState
    ) { query, department, specialization, state ->

        state.doctors.filter { doctor ->

            val matchesSearch =
                query.isBlank() ||
                        doctor.name.contains(
                            query,
                            ignoreCase = true
                        ) ||
                        doctor.specialization.contains(
                            query,
                            ignoreCase = true
                        ) ||
                        doctor.department.contains(
                            query,
                            ignoreCase = true
                        )

            val matchesDepartment =
                department == null ||
                        doctor.department.equals(
                            department,
                            ignoreCase = true
                        )

            val matchesSpecialization =
                specialization == null ||
                        doctor.specialization.equals(
                            specialization,
                            ignoreCase = true
                        )

            matchesSearch &&
                    matchesDepartment &&
                    matchesSpecialization
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // ---------------------------------------------------------
    // INITIAL LOAD
    // ---------------------------------------------------------

    init {
        getDoctors()
    }

    fun getDoctors() {

        Log.d(TAG, "getDoctors() started")

        viewModelScope.launch {

            _doctorState.value = _doctorState.value.copy(
                isLoading = true,
                error = null
            )

            getDoctorsUseCase()
                .onSuccess { doctors ->

                    Log.d(
                        TAG,
                        "Doctors fetched successfully | count=${doctors.size}"
                    )

                    doctors.forEach { doctor ->

                        Log.d(
                            TAG,
                            "Doctor: " +
                                    "id=${doctor.id}, " +
                                    "name=${doctor.name}, " +
                                    "department=${doctor.department}, " +
                                    "specialization=${doctor.specialization}, " +
                                    "rating=${doctor.rating}, " +
                                    "fee=${doctor.consultationFee}"
                        )
                    }

                    _doctorState.value = DoctorListState(
                        isLoading = false,
                        doctors = doctors,
                        error = null
                    )
                }
                .onFailure { exception ->

                    Log.e(
                        TAG,
                        "Failed to fetch doctors",
                        exception
                    )

                    _doctorState.value = DoctorListState(
                        isLoading = false,
                        doctors = emptyList(),
                        error = exception.message
                            ?: "Failed to load doctors"
                    )
                }
        }
    }

    fun onSearchQueryChanged(query: String) {

        Log.d(
            TAG,
            "Search query changed: $query"
        )

        _searchQuery.value = query
    }


    fun clearFilters() {

        Log.d(TAG, "Clearing all filters")

        _selectedDepartment.value = null
        _selectedSpecialization.value = null
    }
    fun setDepartment(department: String?) {
        _selectedDepartment.value = department
    }

    fun setSpecialization(specialization: String?) {
        _selectedSpecialization.value = specialization
    }


    fun retry() {
        getDoctors()
    }
}