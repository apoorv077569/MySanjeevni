package com.mysanjeevni.mysanjeevni.features.labs.presentation.state

import androidx.compose.ui.graphics.Color

data class LabTestState(
    val isLoading: Boolean = false,
    val upcomingTests: List<LabTestItem> = emptyList(),
    val pastTests: List<LabTestItem> = emptyList(),
    val selectedTab: Int = 0
)

data class LabTestItem(
    val id: String,
    val testName: List<String>,
    val labName: String,
    val date: String,
    val time: String,
    val patientName: String,
    val totalAmount: Double,
    val status: TestStatus,
    val isHomeCollection: Boolean = true
)

enum class TestStatus(val label: String,val color: Color){
    SCHEDULED("Schedduled",Color(0xFF1976D2)),
    SAMPLE_COLLECTED("Sample Collected",Color(0xFFFFA000)),
    REPORT_READY("Report Ready",Color(0xFF43A047)),
    CANCELLED("Cancelled",Color(0xFFD32F2F)),
}
