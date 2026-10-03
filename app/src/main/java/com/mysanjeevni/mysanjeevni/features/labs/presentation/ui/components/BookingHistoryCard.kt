package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.BookingHistory
import com.mysanjeevni.mysanjeevni.utils.AutoText


private data class StatusStyle(
    val color: Color,
    val icon: ImageVector,
    val label: String,
)

@Composable
private fun statusStyle(status: String): StatusStyle =
    when (status.lowercase()) {
        "scheduled" ->
            StatusStyle(
                Color(0xFFFFB74D),
                Icons.Default.Schedule,
                "SCHEDULED"
            )

        "completed" ->
            StatusStyle(
                Color(0xFF66BB6A),
                Icons.Default.CheckCircle,
                "COMPLETED"
            )

        "cancelled" ->
            StatusStyle(
                Color(0xFFEF5350),
                Icons.Default.Cancel,
                "CANCELLED"
            )

        else ->
            StatusStyle(
                MaterialTheme.colorScheme.onSurfaceVariant,
                Icons.Default.Info,
                status.uppercase()
            )
    }
private fun testIcon(status: String): ImageVector = when (status.lowercase()) {
    "completed" -> Icons.Default.Assignment
    "cancelled" -> Icons.Default.Cancel
    else        -> Icons.Default.Science
}

// ── main card ──────────────────────────────────────────────────────────────

@Composable
fun BookingHistoryCard(
    booking: BookingHistory,
    onClick: () -> Unit,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val style = statusStyle(booking.status)

    val currencyState by currencyViewModel.state.collectAsState()

    // Fallback to INR (rate = 1.0) while loading or if currency fetch failed
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    val convertedAmount = booking.amount * exchangeRate
    val formattedAmount = "$currencySymbol${
        String.format(
            java.util.Locale.getDefault(),
            if (exchangeRate == 1.0) "%.0f" else "%.2f",
            convertedAmount
        )
    }"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            // Colored left border accent
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(IntrinsicSize.Max)   // stretches to card height
                    .fillMaxHeight()
                    .background(
                        color = style.color,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    )
            )

            // Card body
            Column(modifier = Modifier.padding(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 0.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Icon badge
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(style.color.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = testIcon(booking.status),
                            contentDescription = null,
                            tint = style.color,
                            modifier = Modifier.size(28.dp),
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Name + price + status chip
                    Column(modifier = Modifier.weight(1f)) {
                        AutoText(
                            text = booking.testName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.height(4.dp))
                        AutoText(
                            text = formattedAmount,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(8.dp))
                        StatusChip(style = style)
                    }

                    // Chevron
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View details",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Spacer(Modifier.height(12.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant
                    , thickness = 1.dp)

                // Booked date row
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = style.color,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    AutoText(
                        text = "Booked: ${booking.createdAt}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

// ── status chip ────────────────────────────────────────────────────────────

@Composable
private fun StatusChip(style: StatusStyle) {
    Surface(
        color = style.color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = style.icon,
                contentDescription = null,
                tint = style.color,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.width(4.dp))
            AutoText(
                text = style.label,
                color = style.color,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ── empty state ────────────────────────────────────────────────────────────

@Composable
fun EmptyBookingHistory() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Default.Science,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        AutoText(
            text = "No Lab Bookings Found",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
        )
    }
}