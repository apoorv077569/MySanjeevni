package com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ConsultItem
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ConsultState
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ConsultStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyConsultViewModel : ViewModel() {

    private val _state = MutableStateFlow(ConsultState())
    val state: StateFlow<ConsultState> = _state.asStateFlow()

    init {
        loadConsults()
    }

    private fun loadConsults() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000) // Simulate Network

            val upcoming = listOf(
                ConsultItem(
                    id = "CON-101",
                    doctorName = "Dr. Emily Smith",
                    specialization = "General Physician",
                    hospitalName = "Apollo Clinic",
                    date = "Today, 24 Oct",
                    time = "04:30 PM",
                    status = ConsultStatus.WAITING,
                    isVideoCall = true
                ),
                ConsultItem(
                    id = "CON-102",
                    doctorName = "Dr. Rajeev Kumar",
                    specialization = "Dermatologist",
                    hospitalName = null,
                    date = "Tomorrow, 25 Oct",
                    time = "10:00 AM",
                    status = ConsultStatus.SCHEDULED,
                    isVideoCall = true
                )
            )

            val past = listOf(
                ConsultItem(
                    id = "CON-088",
                    doctorName = "Dr. Anjali Gupta",
                    specialization = "Gynecologist",
                    hospitalName = "City Hospital",
                    date = "15 Sep 2023",
                    time = "11:00 AM",
                    status = ConsultStatus.COMPLETED,
                    isVideoCall = false // In-person
                ),
                ConsultItem(
                    id = "CON-054",
                    doctorName = "Dr. Emily Smith",
                    specialization = "General Physician",
                    hospitalName = "Apollo Clinic",
                    date = "10 Aug 2023",
                    time = "06:00 PM",
                    status = ConsultStatus.CANCELLED,
                    isVideoCall = true
                )
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    upcomingConsults = upcoming,
                    pastConsults = past
                )
            }
        }
    }

    fun onTabSelected(index: Int) {
        _state.update { it.copy(selectedTab = index) }
    }
}