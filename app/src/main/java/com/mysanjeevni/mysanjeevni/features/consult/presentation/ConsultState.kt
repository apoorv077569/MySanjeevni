package com.mysanjeevni.mysanjeevni.features.consult.presentation


import androidx.compose.ui.graphics.Color

enum class ConsultStatus(val label: String, val color: Color) {
    SCHEDULED("Scheduled", Color(0xFF1976D2)),       // Blue
    COMPLETED("Completed", Color(0xFF43A047)),       // Green
    CANCELLED("Cancelled", Color(0xFFD32F2F)),       // Red
    WAITING("Waiting for Doctor", Color(0xFFFFA000)) // Orange
}

data class ConsultItem(
    val id: String,
    val doctorName: String,
    val specialization: String, // e.g., "Cardiologist"
    val hospitalName: String?,  // Nullable if private practice
    val date: String,
    val time: String,
    val status: ConsultStatus,
    val isVideoCall: Boolean = true
)

data class ConsultState(
    val isLoading: Boolean = false,
    val upcomingConsults: List<ConsultItem> = emptyList(),
    val pastConsults: List<ConsultItem> = emptyList(),
    val selectedTab: Int = 0 // 0 = Upcoming, 1 = Past
)