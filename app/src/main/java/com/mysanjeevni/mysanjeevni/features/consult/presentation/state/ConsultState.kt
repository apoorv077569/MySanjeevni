package com.mysanjeevni.mysanjeevni.features.consult.presentation.state

import androidx.compose.ui.graphics.Color
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation

enum class ConsultStatus(
    val label: String,
    val color: Color
) {
    SCHEDULED(
        label = "Scheduled",
        color = Color(0xFF1976D2)
    ),

    COMPLETED(
        label = "Completed",
        color = Color(0xFF43A047)
    ),

    CANCELLED(
        label = "Cancelled",
        color = Color(0xFFD32F2F)
    ),

    WAITING(
        label = "Waiting for Doctor",
        color = Color(0xFFFFA000)
    )
}

data class ConsultItem(
    val id: String,
    val doctorName: String,
    val specialization: String,
    val hospitalName: String?,
    val date: String,
    val time: String,
    val status: ConsultStatus,
    val isVideoCall: Boolean = true,
    val symptoms: String = "",
    val fees: Double = 0.0
)
data class ConsultState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val consultations: List<Consultation> = emptyList(),
    val selectedConsultation: Consultation? = null,
    val upcomingConsults: List<ConsultItem> = emptyList(),
    val pastConsults: List<ConsultItem> = emptyList(),
    val selectedTab: Int = 0
)