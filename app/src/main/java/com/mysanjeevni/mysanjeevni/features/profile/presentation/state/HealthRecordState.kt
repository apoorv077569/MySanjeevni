package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

// Enum to categorize records
enum class RecordType {
    PRESCRIPTION, LAB_REPORT, INVOICE, ALL
}

// 1. Data Model
data class HealthRecordItem(
    val id: String,
    val title: String,          // e.g. "Viral Fever Prescription"
    val doctorOrLabName: String,// e.g. "Dr. Sharma" or "Apollo Diagnostics"
    val date: String,
    val type: RecordType,
    val fileSize: String        // e.g. "2.4 MB"
)

// 2. UI State
data class HealthRecordState(
    val isLoading: Boolean = false,
    val records: List<HealthRecordItem> = emptyList(),
    val filteredRecords: List<HealthRecordItem> = emptyList(),
    val selectedFilter: RecordType = RecordType.ALL
)