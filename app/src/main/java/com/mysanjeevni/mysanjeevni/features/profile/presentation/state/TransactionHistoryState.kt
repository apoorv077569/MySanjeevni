package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

data class TransactionHistoryItem(
    val id:String,
    val title:String,
    val dateHeader:String,
    val time:String,
    val amount: Double,
    val isCredit: Boolean
)
data class TransactionHistoryState(
    val isLoading: Boolean = false,
    val selectedFilter:String="All",
    val groupedTransactions:Map<String, List<TransactionHistoryItem>> = emptyMap(),
    val error:String?=null
)