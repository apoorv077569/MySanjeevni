package com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabTestItem
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabTestState
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.TestStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LabTestViewModel: ViewModel() {
    private val _state = MutableStateFlow(LabTestState())
    val state: StateFlow<LabTestState> = _state.asStateFlow()

    init {
        loadTests()
    }

    private fun loadTests() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(800)

            val upcoming = listOf(
                LabTestItem(
                    "LAB-102",
                    listOf("Full Body Checkup","Vitamin D"),
                    "Apollo Diagnostics",
                    "Tomorrow, 25 Oct",
                    "08:00 AM",
                    "John Doe",
                    1499.0,
                    TestStatus.SCHEDULED,
                    true
                )
            )
            val past = listOf(
                LabTestItem(
                    "LAB-099",
                    listOf("HbA1c (Diabetes Monitor)"),
                    "Thyrocare",
                    "10 Oct 2023",
                    "09:30 AM",
                    "John Doe",
                    450.0,
                    TestStatus.REPORT_READY,
                    false
                ),
                LabTestItem(
                    "LAB-085",
                    listOf("RTPCR Covid-19"),
                    "SRL Diagnostics",
                    "15 Sep 2023",
                    "02:00 PM",
                    "Apoorv Rathore",
                    800.0,
                    TestStatus.REPORT_READY,
                    true
                ),
            )
            _state.update {
                it.copy(
                    isLoading = false,
                    upcomingTests = upcoming,
                    pastTests = past
                )
            }
        }
    }
    fun onTabSelected(index: Int){
        _state.update { it.copy(selectedTab = index) }
    }
}