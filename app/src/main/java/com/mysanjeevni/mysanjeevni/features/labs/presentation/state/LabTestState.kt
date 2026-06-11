package com.mysanjeevni.mysanjeevni.features.labs.presentation.state

import androidx.compose.ui.graphics.Color
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.PaginationDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest

data class LabTestState(
    val isLoading: Boolean = false,

    val selectedTab: Int = 0,

    val upcomingTests: List<LabTest> = emptyList(),

    val pastTests: List<LabTest> = emptyList(),

    val error: String? = null,
    val pagination: PaginationDto? = null   // 👈 add this
)

enum class TestStatus(val label: String,val color: Color){
    SCHEDULED("Schedduled",Color(0xFF1976D2)),
    SAMPLE_COLLECTED("Sample Collected",Color(0xFFFFA000)),
    REPORT_READY("Report Ready",Color(0xFF43A047)),
    CANCELLED("Cancelled",Color(0xFFD32F2F)),
}
