package com.mysanjeevni.mysanjeevni.features.doctor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.AppointmentStatus
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.DoctorAppointmentItem
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.DoctorDashboardState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val api: AuthApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(DoctorDashboardState())
    val state: StateFlow<DoctorDashboardState> = _state.asStateFlow()
    val token = sessionManager.getToken()


    init {
        loadDashboard()
        loadDoctorProfile() // ✅ yahi se call hoga
    }

    fun loadDoctorProfile() {
        viewModelScope.launch {
            try {
                val token = sessionManager.getToken()

                val res = api.getProfile("Bearer $token")

                if (res.isSuccessful) {
                    val user = res.body()?.user

                    _state.update {
                        it.copy(
                            doctorName = user?.fullName ?: "",
                            specialization = user?.role ?: "Doctor"
                        )
                    }
                }

            } catch (e: Exception) {
                Log.e("DOCTOR_PROFILE", e.message.toString())
            }
        }
    }
    private fun loadDashboard() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000)

            val appointments = listOf(
                DoctorAppointmentItem(
                    "1","Rahul Verma","24 M","Severe Migraine",
                    "10:00 AM","Video Call", AppointmentStatus.WAITING
                ),
                DoctorAppointmentItem(
                    "2","Arya Singh","24 M","Mild Fever",
                    "10:30 AM","Video Call", AppointmentStatus.UPCOMING
                )
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    totalPatientsToday = 12,
                    pendingAppointments = 8,
                    totalEarnings = 4500.00,
                    appointments = appointments
                )
            }
        }
    }

    fun toggleAvailability(isOnline: Boolean) {
        _state.update { it.copy(isOnline = isOnline) }
    }
}