package com.mysanjeevni.mysanjeevni.features.doctor.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.DoctorAppointmentItem
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.state.DoctorDashboardState
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.viewmodel.DoctorViewModel

@Composable
fun DoctorDashboardScreen(
    navController: NavController,
    viewModel: DoctorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // 🔥 HEADER
            item {
                DoctorHeader(state)
            }

            // 🔥 STATS
            item {
                StatsSection(state)
            }

            // 🔥 STATUS STRIP
            item {
                StatusStrip(state)
            }

            // 🔥 APPOINTMENTS TITLE
            item {
                Text(
                    "Today's Appointments",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 🔥 APPOINTMENT LIST
            items(state.appointments) {
                ModernAppointmentCard(it)
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun DoctorHeader(state: DoctorDashboardState) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF00C853), Color(0xFF009688))
                )
            )
            .padding(20.dp)
    ) {

        Column {

            Text(
                text = "👋 Welcome Back",
                color = Color.White.copy(0.8f),
                fontSize = 14.sp
            )

            Text(
                text = "${state.doctorName}",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = if (state.isOnline) "🟢 Online" else "🔴 Offline",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )

                Switch(
                    checked = state.isOnline,
                    onCheckedChange = { /* toggle */ }
                )
            }
        }
    }
}

@Composable
fun StatsSection(state: DoctorDashboardState) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ModernStatCard("Patients", state.totalPatientsToday.toString())
        ModernStatCard("Pending", state.pendingAppointments.toString())
        ModernStatCard("Earnings", "₹${state.totalEarnings.toInt()}")
    }
}

@Composable
fun ModernStatCard(title: String, value: String) {

    Card(
        modifier = Modifier.width(110.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun StatusStrip(state: DoctorDashboardState) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text("Queue", fontWeight = FontWeight.Bold)
                Text("${state.pendingAppointments} patients waiting")
            }

            Button(onClick = {}) {
                Text("View Queue")
            }
        }
    }
}

@Composable
fun ModernAppointmentCard(item: DoctorAppointmentItem) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(item.patientName, fontWeight = FontWeight.Bold)
                    Text(item.ageGender, fontSize = 12.sp, color = Color.Gray)
                }

                Text(item.status.label, color = item.status.color)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("🩺 ${item.symptom}")
            Text("⏰ ${item.time}", color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Call")
                }

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Prescription")
                }
            }
        }
    }
}

