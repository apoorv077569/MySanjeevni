package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.HealthRecordItem
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.HealthRecordState
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.RecordType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HealthRecordViewModel : ViewModel() {

    private val _state = MutableStateFlow(HealthRecordState())
    val state: StateFlow<HealthRecordState> = _state.asStateFlow()

    init {
        loadRecords()
    }

    private fun loadRecords() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(800) // Simulate network

            val mockData = listOf(
                HealthRecordItem("1", "General Checkup Rx", "Dr. A. Kumar", "24 Oct 2023", RecordType.PRESCRIPTION, "1.2 MB"),
                HealthRecordItem("2", "Thyroid Profile", "Thyrocare Labs", "20 Oct 2023", RecordType.LAB_REPORT, "3.5 MB"),
                HealthRecordItem("3", "Medicine Invoice", "Netmeds Pharmacy", "18 Oct 2023", RecordType.INVOICE, "0.5 MB"),
                HealthRecordItem("4", "Skin Consultation", "Dr. S. Gupta", "10 Sep 2023", RecordType.PRESCRIPTION, "2.1 MB"),
                HealthRecordItem("5", "CBC Blood Test", "Lal PathLabs", "01 Sep 2023", RecordType.LAB_REPORT, "1.8 MB"),
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    records = mockData,
                    filteredRecords = mockData
                )
            }
        }
    }

    fun onFilterSelected(type: RecordType) {
        _state.update { currentState ->
            val filtered = if (type == RecordType.ALL) {
                currentState.records
            } else {
                currentState.records.filter { it.type == type }
            }
            currentState.copy(selectedFilter = type, filteredRecords = filtered)
        }
    }
}