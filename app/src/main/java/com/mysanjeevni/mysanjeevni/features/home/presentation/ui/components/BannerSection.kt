package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.LavenderPrimary
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.TealAccent
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun PrescriptionActionCard(navController: NavController) {
//    val isDark = isSystemInDarkTheme()
    val isDark = LocalIsDarkTheme.current
    val cardBg = if (isDark) Color(0xFF15302D) else Color(0xFFE0F2F1)
    val textColor = if (isDark) Color.White else Color.Black

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween  // ✅ SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)  // ✅ weight(1f) diya
            ) {
                Icon(Icons.Default.Description, null, tint = TealAccent, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                AutoText("Order with prescription", fontWeight = FontWeight.Bold, color = textColor, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))  // ✅ gap before button

            Button(
                onClick = { navController.navigate(Screen.UploadPrescription.route) },
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),  // ✅ proper padding
                modifier = Modifier.wrapContentSize()
            ) {
                AutoText("Upload Now", fontSize = 12.sp, maxLines = 1, color = Color.White)  // ✅ maxLines = 1
            }
        }
    }
}

@Composable
fun ActionGridSection() {
//    val isDark = isSystemInDarkTheme()
    val isDark = LocalIsDarkTheme.current
    val greenCardBg = if (isDark) Color(0xFF1B2A14) else Color(0xFFF1F8E9)
    val purpleCardBg = if (isDark) Color(0xFF2A2030) else Color(0xFFF3E5F5)
    val textColor = if (isDark) Color.White else Color.Black
    val mutedColor = if (isDark) Color(0xFFAAAAAA) else Color.Gray
    val greenIconTint = if (isDark) Color(0xFF9CCC65) else Color(0xFF558B2F)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = greenCardBg)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AutoText("No Prescription?", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textColor)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                        null,
                        tint = greenIconTint,
                        modifier = Modifier.size(10.dp)
                    )
                }
                AutoText("Get FREE Consultation!", fontSize = 11.sp, color = mutedColor)
            }
        }
        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = purpleCardBg)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AutoText("Call to order", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textColor)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Call, null, tint = LavenderPrimary, modifier = Modifier.size(14.dp))
                }
                AutoText("Our team will assist you.", fontSize = 11.sp, color = mutedColor)
            }
        }
    }
}

@Composable
fun ZeroFeeBanner() {
    // Intentionally kept as a fixed brand-color promo banner in both themes —
    // it's a vivid accent strip, not a content surface, so it doesn't need to
    // shift with light/dark mode.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF38D6C6)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Stars, null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            AutoText("ZERO ", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD54F))
            AutoText("Handling Charges", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}