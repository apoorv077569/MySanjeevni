package com.mysanjeevni.mysanjeevni.features.health.presentation.viemodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.health.domain.model.HealthArticle
import com.mysanjeevni.mysanjeevni.features.health.domain.model.HealthTool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor() : ViewModel() {

    // 1. Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. Data Sources (Moved from UI)
    private val _tools = MutableStateFlow(
        listOf(
            HealthTool("BMI Calculator", "https://cdn-icons-png.flaticon.com/512/3373/3373123.png"),
            HealthTool("Period Tracker", "https://cdn-icons-png.flaticon.com/512/2747/2747059.png"),
            HealthTool("Calorie Counter", "https://cdn-icons-png.flaticon.com/512/1043/1043445.png"),
            HealthTool("Pregnancy Guide", "https://cdn-icons-png.flaticon.com/512/3050/3050225.png")
        )
    )

    private val _articles = MutableStateFlow(
        listOf(
            HealthArticle("10 Superfoods for Immunity", "Nutrition", "https://img.freepik.com/free-photo/fresh-fruit-stalls-san-miguel-market_53876-146829.jpg", "5 min read"),
            HealthArticle("Yoga for Back Pain", "Fitness", "https://img.freepik.com/free-photo/young-woman-doing-yoga-mat_23-2148025178.jpg", "8 min read"),
            HealthArticle("Signs of Vitamin D Deficiency", "Wellness", "https://img.freepik.com/free-photo/doctor-holding-vitamin-d-pill_23-2148768846.jpg", "4 min read")
        )
    )

    // 3. Combined Filter Logic
    // This creates a "HealthSearchResult" object containing matches
    val searchResults = combine(_searchQuery, _tools, _articles) { query, tools, articles ->
        if (query.isBlank()) {
            HealthSearchResults() // Empty result if not searching
        } else {
            HealthSearchResults(
                tools = tools.filter { it.name.contains(query, ignoreCase = true) },
                articles = articles.filter {
                    it.title.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
                }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HealthSearchResults())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

// Helper data class for results
data class HealthSearchResults(
    val tools: List<HealthTool> = emptyList(),
    val articles: List<HealthArticle> = emptyList()
)