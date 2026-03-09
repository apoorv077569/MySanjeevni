package com.mysanjeevni.mysanjeevni.features.plan.presentation.ui

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(navController: NavController) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    val golGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFD700), Color(0xFFFF8F00))
    )
    var selectedPlanIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            PlanBottomBar(isDark)
        },
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
//            Premium Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(golGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Care Plan Member",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Save extra 5% on every order + Free Delivery",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset(y = (-30).dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Estimated Savings", color = Color.Gray, fontSize = 14.sp)
                    Text("₹ 4,500 / year", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00C853))
                }
            }
            Text(
                "Membership Benefits",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            BenefitItem("Free Delivery on all orders above ₹499", cardColor, textColor)
            BenefitItem("Extra 5% Cashback on Lab Tests", cardColor, textColor)
            BenefitItem("Free Doctor Consultation (Once a month)", cardColor, textColor)
            BenefitItem("Priority Customer Support", cardColor, textColor)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Choose your Plan",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlanSelectionCard(
                    title = "3 Months",
                    price = "₹199",
                    oldPrice = "₹299",
                    isSelected = selectedPlanIndex == 0,
                    onClick = { selectedPlanIndex = 0 },
                    modifier = Modifier.weight(1f),
                    cardColor = cardColor,
                    textColor = textColor
                )
                PlanSelectionCard(
                    title = "3 Months",
                    price = "₹299",
                    oldPrice = "₹1499",
                    isSelected = selectedPlanIndex == 1,
                    onClick = { selectedPlanIndex = 1 },
                    modifier = Modifier.weight(1f),
                    cardColor = cardColor,
                    textColor = textColor
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// Components

@Composable
fun BenefitItem(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF00C853),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, color = textColor)
    }
}

@Composable
fun PlanBottomBar(isDark: Boolean) {
    val containerColor = if(isDark) Color(0xFF1E1E1E) else Color.White

    Surface(shadowElevation = 16.dp, color = containerColor) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor=Color(0xFFFF8F00)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Join Now", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun PlanSelectionCard(
    title: String,
    price: String,
    oldPrice: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    cardColor: Color,
    textColor: Color
) {
    val borderColor = if (isSelected) Color(0xFFFF8F00) else Color.Transparent
    val borderWidth = if (isSelected) 2.dp else 0.dp

    Card(
        modifier = Modifier
            .clickable { onClick() }
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(price, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textColor)
            Text(
                oldPrice,
                fontSize = 1.sp,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )
            if(isSelected){
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFFFF8F00),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}