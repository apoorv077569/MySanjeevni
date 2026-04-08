package com.mysanjeevni.mysanjeevni.features.doctor.presentation.state

import androidx.compose.ui.graphics.Color

enum class AppointmentStatus(val label: String, val color: Color) {
    UPCOMING("Upcoming", Color(0xFF1976D2)),
    WAITING("Waiting in Lobby", Color(0xFFFFA000)),
    COMPLETED("Completed", Color(0xFF43A047)),
    CANCELLED("Cancelled", Color(0xFFD32F2F))
}

data class DoctorAppointmentItem(
    val id: String,
    val patientName: String,
    val ageGender: String,
    val symptom: String,
    val time: String,
    val type: String,
    val status: AppointmentStatus
)

data class DoctorDashboardState(
    val isLoading: Boolean = false,
    val doctorName: String = "Dr. Aarya",
    val isOnline: Boolean = true,
    val totalPatientsToday: Int = 0,
    val pendingAppointments: Int = 0,
    val totalEarnings: Double = 0.0,
    val appointments: List<DoctorAppointmentItem> = emptyList()
)