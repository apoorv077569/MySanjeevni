package com.mysanjeevni.mysanjeevni.features.profile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.HealthRecordItem
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.RecordType
import com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel.HealthRecordViewModel

@Composable
fun HealthRecordsScreen(
    navController: NavController,
    viewModel: HealthRecordViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Theme Colors
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryText = if (isDark) Color.LightGray else Color.Gray
    val primaryColor = MaterialTheme.colorScheme.primary

    Scaffold(
        containerColor = bgColor,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* Open File Picker */ },
                containerColor = primaryColor,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.FileUpload, null) },
                text = { Text("Upload New") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Medical Records",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            // 2. Filters
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(RecordType.entries.toTypedArray()) { type ->
                    val label = when(type) {
                        RecordType.ALL -> "All"
                        RecordType.PRESCRIPTION -> "Prescriptions"
                        RecordType.LAB_REPORT -> "Lab Reports"
                        RecordType.INVOICE -> "Invoices"
                    }

                    FilterChip(
                        selected = state.selectedFilter == type,
                        onClick = { viewModel.onFilterSelected(type) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = primaryColor,
                            selectedLabelColor = Color.White,
                            containerColor = cardColor,
                            labelColor = textColor
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = state.selectedFilter == type,
                            borderColor = if(state.selectedFilter == type) primaryColor else Color.Transparent
                        )
                    )
                }
            }

            // 3. List
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            } else if (state.filteredRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No records found", color = secondaryText)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.filteredRecords) { record ->
                        RecordItem(record, cardColor, textColor, secondaryText, primaryColor)
                    }
                }
            }
        }
    }
}

@Composable
fun RecordItem(
    record: HealthRecordItem,
    cardColor: Color,
    textColor: Color,
    secondaryText: Color,
    primaryColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Background based on Type
            val iconBg = when(record.type) {
                RecordType.PRESCRIPTION -> Color(0xFFE3F2FD) // Light Blue
                RecordType.LAB_REPORT -> Color(0xFFF3E5F5)   // Light Purple
                else -> Color(0xFFEEEEEE)                    // Grey
            }
            val iconTint = when(record.type) {
                RecordType.PRESCRIPTION -> Color(0xFF1976D2)
                RecordType.LAB_REPORT -> Color(0xFF7B1FA2)
                else -> Color.Gray
            }
            val iconVector = when(record.type) {
                RecordType.PRESCRIPTION -> Icons.Default.Description
                RecordType.LAB_REPORT -> Icons.Default.Science
                else -> Icons.Default.Receipt
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconVector, null, tint = iconTint)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    text = record.doctorOrLabName,
                    fontSize = 14.sp,
                    color = secondaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = record.date,
                        fontSize = 12.sp,
                        color = secondaryText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(4.dp).clip(RoundedCornerShape(2.dp)).background(secondaryText))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = record.fileSize,
                        fontSize = 12.sp,
                        color = secondaryText
                    )
                }
            }

            // Action
            IconButton(onClick = { /* Download */ }) {
                Icon(Icons.Default.Download, contentDescription = "Download", tint = primaryColor)
            }
        }
    }
}