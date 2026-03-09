package com.mysanjeevni.mysanjeevni.features.profile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R

// dummy data

data class HealthRecordItem(
    val id: Int,
    val title: String,
    val date: String,
    val type: RecordType,
    val doctorName: String? = null
)

enum class RecordType { PRESCRIPTION, LAB_REPORT, INVOICE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordsScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Prescriptions", "Lab Reports", "Invoices")

//     mock data
    val allRecords = listOf(
        HealthRecordItem(
            1,
            "Skin Infection",
            "12 Feb 2026",
            RecordType.PRESCRIPTION,
            "Dr. A. Sharma"
        ),
        HealthRecordItem(
            2,
            "Thyroid Profile",
            "15 Feb 2026",
            RecordType.LAB_REPORT,

            ),
        HealthRecordItem(
            2,
            "Vitamin D Test",
            "20 Feb 2026",
            RecordType.LAB_REPORT
        ),
        HealthRecordItem(
            4,
            "Fever Medicine",
            "21 Feb 2026",
            RecordType.PRESCRIPTION,
            "Dr. P. Verma"
        ),
        HealthRecordItem(
            5,
            "Medicine Bill #9923",
            "3 March 2026",
            RecordType.INVOICE
        )
    )
//    filter logic
    val filterRecords = when (selectedFilter) {
        "Prescriptions" -> allRecords.filter { it.type == RecordType.PRESCRIPTION }
        "Lab Reports" -> allRecords.filter { it.type == RecordType.LAB_REPORT }
        "Invoices" -> allRecords.filter { it.type == RecordType.INVOICE }
        else -> allRecords
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Health Records") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload New")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(text = filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
//            Record List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (filterRecords.isEmpty()) {
                    item {
                        EmptyStateView()
                    }
                } else {
                    items(filterRecords) { record ->
                        RecordCard(record)
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun RecordCard(record: HealthRecordItem) {
    val icon = when(record.type){
        RecordType.PRESCRIPTION -> Icons.Default.Description
        RecordType.LAB_REPORT -> Icons.Default.Science
            RecordType.INVOICE -> Icons.Default.Receipt
    }
    val color = when(record.type){
        RecordType.PRESCRIPTION -> Color(0xFFE3F2FD)
        RecordType.LAB_REPORT -> Color(0xFFE8F5E9)
        RecordType.INVOICE -> Color(0xFFFFF3E0)
    }
    val iconTint =when(record.type){
        RecordType.PRESCRIPTION -> Color(0xFF1976D2)
        RecordType.LAB_REPORT -> Color(0xFF388E3C)
        RecordType.INVOICE -> Color(0xFFF57C00)
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    icon,
                    null,
                  tint =   iconTint
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

//            Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    record.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = record.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (record.doctorName != null){
                    Text(
                        text = record.doctorName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    "Options",
                    tint =  Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Records Found",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = "Upload prescriptions or reports to keep them handy.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )
    }
}