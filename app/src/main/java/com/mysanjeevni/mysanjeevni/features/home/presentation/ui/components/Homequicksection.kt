package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun QuickActions(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Medication,
            iconBg = Color(0xFFE8F8F4),
            iconTint = AppGreen,
            title = "Medicines",
            subtitle = "Health at your doorstep",
            onClick = {}
        )
        QuickActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Science,
            iconBg = Color(0xFFF0F4FF),
            iconTint = Color(0xFF3F72E0),
            title = "Lab Tests",
            subtitle = "Accurate & reliable reports",
            onClick = {}
        )
        QuickActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Apps,
            iconBg = Color(0xFFFFF4EC),
            iconTint = Color(0xFFFF7043),
            title = "Categories",
            subtitle = "All health categories",
            onClick = {}
        )
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            AutoText(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            AutoText(subtitle, fontSize = 10.sp, color = Color.Gray, lineHeight = 13.sp)
        }
    }
}

@Composable
fun PrescriptionSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Upload Prescription
        Card(
            modifier = Modifier.weight(1.5f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F4)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(AppGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Description, null, tint = AppGreen, modifier = Modifier.size(22.dp))
                }
                Column {
                    AutoText("Order with Prescription", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    AutoText("Upload & get medicines delivered", fontSize = 10.sp, color = Color.Gray)
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppGreen)
                            .clickable { navController.navigate(Screen.UploadPrescription.route) }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        AutoText("Upload Now", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Book Lab Tests
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Science, null, tint = Color(0xFF3F72E0), modifier = Modifier.size(22.dp))
                    Column {
                        AutoText("Book Lab Tests", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        AutoText("Accurate reports at best prices", fontSize = 9.sp, color = Color.Gray)
                    }
                }
            }
            // Call to Order
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4EC)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Call, null, tint = Color(0xFFFF7043), modifier = Modifier.size(22.dp))
                    Column {
                        AutoText("Call to Order", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        AutoText("Talk to our experts and order", fontSize = 9.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}