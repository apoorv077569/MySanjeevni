package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ConsultItem
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ConsultStatus
import com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel.MyConsultViewModel

@Composable
fun MyConsultsScreen(
    navController: NavController,
    viewModel: MyConsultViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Theme logic
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryText = if (isDark) Color.LightGray else Color.Gray
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
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
                text = "My Consultations",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // 2. Tabs
        TabRow(
            selectedTabIndex = state.selectedTab,
            containerColor = cardColor,
            contentColor = primaryColor,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[state.selectedTab]),
                    color = primaryColor
                )
            }
        ) {
            Tab(
                selected = state.selectedTab == 0,
                onClick = { viewModel.onTabSelected(0) },
                text = { Text("Upcoming") }
            )
            Tab(
                selected = state.selectedTab == 1,
                onClick = { viewModel.onTabSelected(1) },
                text = { Text("Past Consults") }
            )
        }

        // 3. Content
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else {
            val listToShow = if (state.selectedTab == 0) state.upcomingConsults else state.pastConsults

            if (listToShow.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No consultations found", color = secondaryText)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(listToShow) { consult ->
                        ConsultCard(consult, cardColor, textColor, secondaryText, primaryColor)
                    }
                }
            }
        }
    }
}

@Composable
fun ConsultCard(
    consult: ConsultItem,
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
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Doctor Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = consult.doctorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = textColor
                    )
                    Text(
                        text = consult.specialization,
                        fontSize = 13.sp,
                        color = secondaryText
                    )
                    if (consult.hospitalName != null) {
                        Text(
                            text = consult.hospitalName,
                            fontSize = 12.sp,
                            color = secondaryText
                        )
                    }
                }

                // Status Chip
                Surface(
                    color = consult.status.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = consult.status.label,
                        color = consult.status.color,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = secondaryText.copy(alpha = 0.1f)
            )

            // Date & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, tint = secondaryText, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = consult.date, fontSize = 14.sp, color = textColor)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null, tint = secondaryText, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = consult.time, fontSize = 14.sp, color = textColor)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if(consult.isVideoCall) Icons.Default.VideoCall else Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = secondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if(consult.isVideoCall) "Video" else "Visit", fontSize = 14.sp, color = textColor)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (consult.status == ConsultStatus.WAITING || consult.status == ConsultStatus.SCHEDULED) {
                    // Primary Action: Join or Reschedule
                    Button(
                        onClick = { /* Join Call logic */ },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        modifier = Modifier.weight(1f),
                        enabled = consult.status == ConsultStatus.WAITING, // Only enable if Waiting/Live
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.VideoCall, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Join Call")
                    }

                    OutlinedButton(
                        onClick = { /* Cancel/Reschedule */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, secondaryText)
                    ) {
                        Text("Reschedule", color = textColor)
                    }
                } else if (consult.status == ConsultStatus.COMPLETED) {
                    // Past Actions
                    OutlinedButton(
                        onClick = { /* View Rx */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, primaryColor)
                    ) {
                        Icon(Icons.Default.Description, null, tint = primaryColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Rx", color = primaryColor)
                    }

                    Button(
                        onClick = { /* Book Again */ },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Book Again")
                    }
                }
            }
        }
    }
}