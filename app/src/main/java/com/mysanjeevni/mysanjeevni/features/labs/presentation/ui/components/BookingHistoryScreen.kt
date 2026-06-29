package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.BookingHistoryViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun BookingHistoryScreen(
    navController: NavController,
    viewModel: BookingHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var selectedFilter by remember { mutableStateOf("all") }
    val filters = listOf("all", "scheduled", "completed", "cancelled")
    val colorScheme = MaterialTheme.colorScheme

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.bookings.isEmpty() -> {
            EmptyBookingHistory()
        }

        else -> {
            val filteredBookings = if (selectedFilter == "all") state.bookings
            else state.bookings.filter { it.status.lowercase() == selectedFilter }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filters.forEach { filter ->
                            val isSelected = selectedFilter == filter
                            val (chipColor, chipBg) = when (filter) {
                                "scheduled" -> Color(0xFFFF9800) to Color(0xFFFFF3E0)
                                "completed" -> Color(0xFF4CAF50) to Color(0xFFE8F5E9)
                                "cancelled" -> Color(0xFFF44336) to Color(0xFFFFEBEE)
                                else        -> Color(0xFF7B61FF) to Color(0xFFEDE8F8)
                            }
                            Surface(
                                modifier = Modifier.clickable { selectedFilter = filter },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) chipBg else colorScheme.surfaceVariant,
                                border = if (isSelected)
                                    BorderStroke(1.5.dp, chipColor)
                                else
                                    BorderStroke(
                                        1.dp,
                                        colorScheme.outline.copy(alpha = 0.4f)
                                    )
                            ) {
                                AutoText(
                                    text = filter.replaceFirstChar { it.uppercase() },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected)
                                        chipColor
                                    else
                                        colorScheme.onSurfaceVariant                                )
                            }
                        }
                    }
                }
                if (filteredBookings.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyBookingHistory()
                        }
                    }
                } else {
                    items(filteredBookings) { booking ->  // ✅ filteredBookings
                        BookingHistoryCard(
                            booking = booking,
                            onClick = {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        "booking",
                                        booking
                                    )
                                navController.navigate(
                                    Screen.LabBookingDetail.route
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}