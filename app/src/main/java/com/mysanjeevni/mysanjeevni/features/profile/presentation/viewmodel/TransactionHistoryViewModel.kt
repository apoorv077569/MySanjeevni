package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.TransactionHistoryItem
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.TransactionHistoryState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionHistoryViewModel: ViewModel() {
    private val _state = MutableStateFlow(TransactionHistoryState())
    val state: StateFlow<TransactionHistoryState> = _state.asStateFlow()

    private var allTransactions: List<TransactionHistoryItem> = emptyList()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)

            allTransactions = listOf(
                TransactionHistoryItem("1", "Pharmacy Purchase", "Today", "10:30 AM", -450.0, false),
                TransactionHistoryItem("2", "Wallet Top-up", "Today", "09:00 AM", 500.0, true),
                TransactionHistoryItem("3", "Refund: Order #1234", "Yesterday", "4:15 PM", 1200.0, true),
                TransactionHistoryItem("4", "Lab Test Booking", "Yesterday", "02:30 PM", -850.0, false),
                TransactionHistoryItem("5", "Consultation Fee", "20 Oct 2023", "11:00 AM", -400.0, false),
                TransactionHistoryItem("6", "Referral Bonus", "20 Oct 2023", "08:00 AM", 100.0, true),
                TransactionHistoryItem("7", "Medicine Order", "15 Oct 2023", "06:45 PM", -1250.0, false),
            )
            applyFilter("All")
        }
    }
    fun onFilterSelected(filter:String){
        _state.update { it.copy(selectedFilter = filter) }
        applyFilter(filter)
    }
    private fun applyFilter(filter: String){
        val filteredList = when(filter){
            "Received" -> allTransactions.filter { it.isCredit }
            "Spent" -> allTransactions.filter { !it.isCredit }
            else -> allTransactions
        }
        _state.update {
            it.copy(
                isLoading = false,
                groupedTransactions = filteredList.groupBy { item ->  item.dateHeader }
            )
        }
    }
}