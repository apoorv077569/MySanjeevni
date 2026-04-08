package com.mysanjeevni.mysanjeevni.features.profile.presentation.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen

@Composable
fun WalletScreen(navController: NavController) {
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
            .verticalScroll(rememberScrollState())
    ) {
        // --- Top Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() })
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "My Wallet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // --- Main Balance Card ---
        // FIX: Removed the nested Inner Card. Content is now directly inside the Primary Color Card.
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = primaryColor),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Total Balance",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "₹ 2,450.00",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Add Money Button
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = primaryColor
                    ),
                    modifier = Modifier.fillMaxWidth().height(45.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Money", fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Payment Methods / History ---
        Text(
            text = "Payment Methods",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
        ) {
            ProfileMenuItem(
                icon = Icons.Default.History,
                title = "Payment History",
                textColor = textColor,
                iconColor = secondaryText,
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Recent Transactions ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Transactions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = "See All",
                fontSize = 14.sp,
                color = primaryColor,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { navController.navigate(Screen.TransactionHistoryScreen.route) }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Transaction List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
        ) {
            val transactions = listOf(
                Transaction("Pharmacy Purchase", "Today, 10:30 AM", -450.0, false),
                Transaction("Refund: Order #1234", "Yesterday, 4:15 PM", 1200.0, true),
                Transaction("Wallet Top-up", "20 Oct, 09:00 AM", 500.0, true),
                Transaction("Lab Test Booking", "18 Oct, 02:30 PM", -850.0, false),
            )
            transactions.forEachIndexed{index,transaction ->
                TransactionItem(
                    transaction = transaction,
                    textColor = textColor,
                    secondaryText = secondaryText
                )
                if (index < transactions.lastIndex){
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 56.dp),
                        thickness = DividerDefaults.Thickness,
                        color = bgColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

// --- Data Models & Helpers ---

data class Transaction(
    val title:String,
    val date:String,
    val amount: Double,
    val isCredit: Boolean
)

@Composable
fun TransactionItem(
    transaction: Transaction,
    textColor: Color,
    secondaryText: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (transaction.isCredit) Color(0xFFE8F5E9) // Light Green
                    else Color(0xFFFFEBEE) // Light Red
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (transaction.isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = if (transaction.isCredit) Color(0xFF43A047) else Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and Date
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.date,
                fontSize = 12.sp,
                color = secondaryText
            )
        }

        // Amount
        Text(
            text = "${if (transaction.isCredit) "+" else "-"} ₹${kotlin.math.abs(transaction.amount)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (transaction.isCredit) Color(0xFF43A047) else Color(0xFFE53935)
        )
    }
}

