package com.mysanjeevni.mysanjeevni.features.doctor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.AppointmentStatus
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.DoctorAppointmentItem
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.DoctorDashboardState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorViewModel: ViewModel() {
    private val _state = MutableStateFlow(DoctorDashboardState())
    val state: StateFlow<DoctorDashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)

            val appointments = listOf(
                DoctorAppointmentItem(
                    "1",
                    "Rahul Verma",
                    "24 M",
                    "Severe Migraine",
                    "10:00 AM",
                    "Video Call",
                    AppointmentStatus.WAITING
                ),
                DoctorAppointmentItem(
                    "2",
                    "Arya Singh",
                    "24 M",
                    "Mild Fever",
                    "10:30 AM",
                    "Video Call",
                    AppointmentStatus.UPCOMING
                ),
                DoctorAppointmentItem(
                    "3",
                    "Tanvi Yadav",
                    "21 F",
                    "Skin Rash",
                    "09:00 AM",
                    "Clinic Visit",
                    AppointmentStatus.CANCELLED
                ),
                DoctorAppointmentItem(
                    "4",
                    "Rashmi Rani",
                    "25 F",
                    "Dengue",
                    "05:51 PM",
                    "Video Call",
                    AppointmentStatus.CANCELLED
                ),
            )
            _state.update {
                it.copy(
                    isLoading = false,
                    doctorName = "Dr. Aarya Yadav",
                    totalPatientsToday = 12,
                    pendingAppointments = 8,
                    totalEarnings = 4500.00,
                    appointments = appointments
                )
            }
        }
    }
    fun toggleAvailability(isOnline: Boolean){
        _state.update { it.copy(isOnline = isOnline) }
    }
    fun markAsCompleted(id:String){
//        logic to be implemented
    }
}