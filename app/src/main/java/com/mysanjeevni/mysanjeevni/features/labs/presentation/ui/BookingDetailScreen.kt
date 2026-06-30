package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.BookingHistory
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.BookingHistoryViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.getDaysRemaining

private val Purple = Color(0xFF7B61FF)
private val Orange = Color(0xFFFF9800)
private val GreenText = Color(0xFF4CAF50)

private data class BookingTheme(
    val screenBg: Color,
    val cardBg: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val purpleChipBg: Color,
    val orangeCardBg: Color,
    val orangeIconBg: Color,
    val orangeChipBg: Color,
    val greenChipBg: Color,
    val divider: Color,
)

@Composable
private fun rememberBookingTheme(): BookingTheme {
//    val isDark = isSystemInDarkTheme()
    val isDark = LocalIsDarkTheme.current || isSystemInDarkTheme()
    return if (isDark) {
        BookingTheme(
            screenBg = Color(0xFF121212),
            cardBg = Color(0xFF1E1E1E),
            textPrimary = Color(0xFFF2F2F2),
            textMuted = Color(0xFFA0A0A0),
            purpleChipBg = Color(0xFF2A2440),
            orangeCardBg = Color(0xFF2E2415),
            orangeIconBg = Orange.copy(alpha = 0.22f),
            orangeChipBg = Orange.copy(alpha = 0.22f),
            greenChipBg = Color(0xFF1B3320),
            divider = Color.White.copy(alpha = 0.08f),
        )
    } else {
        BookingTheme(
            screenBg = Color(0xFFF3F4F8),
            cardBg = Color.White,
            textPrimary = Color(0xFF1A1A2E),
            textMuted = Color(0xFF888888),
            purpleChipBg = Color(0xFFEDE8F8),
            orangeCardBg = Color(0xFFFFF3E0),
            orangeIconBg = Orange.copy(alpha = 0.18f),
            orangeChipBg = Orange.copy(alpha = 0.15f),
            greenChipBg = Color(0xFFE8F5E9),
            divider = Color(0xFFF0F0F0),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    navController: NavController
) {
    val historyViewModel: BookingHistoryViewModel = hiltViewModel()
    val state by historyViewModel.state.collectAsState()
    val booking = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<BookingHistory>("booking")
    var showCancelDialog by remember { mutableStateOf(false) }
    val theme = rememberBookingTheme()

    if (booking == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AutoText("Booking Not Found", color = theme.textPrimary)
        }
    } else {

        LaunchedEffect(state.cancelSuccess) {
            if (state.cancelSuccess) {
                navController.popBackStack()
            }
        }

        Scaffold(
            containerColor = theme.screenBg,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = theme.screenBg,
                    ),
                    title = {
                        Column {
                            AutoText(
                                text = "Booking Details",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = theme.textPrimary,
                            )
                            AutoText(
                                text = "View all information about this booking",
                                fontSize = 12.sp,
                                color = theme.textMuted,
                            )
                        }
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.purpleChipBg),
                            contentAlignment = Alignment.Center,
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = theme.textPrimary,
                                )
                            }
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.purpleChipBg),
                            contentAlignment = Alignment.Center,
                        ) {
                            IconButton(
                                onClick = {
                                    historyViewModel.syncBooking(
                                        booking.id
                                    )
                                }
                            ) {
                                if (state.isSyncing) {

                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )

                                } else {

                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Sync",
                                        tint = Purple
                                    )
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Button(
                        onClick = { showCancelDialog = true },
                        enabled = !state.isCancelling,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Teal
                        ),
                    ) {
                        if (state.isCancelling) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            AutoText(
                                text = "Cancel Booking",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = theme.orangeCardBg),
                    elevation = CardDefaults.cardElevation(0.dp),
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Orange.copy(alpha = 0.15f),
                            modifier = Modifier
                                .size(90.dp)
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 8.dp),
                        )
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(theme.orangeIconBg),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    Icons.Default.Science,
                                    contentDescription = null,
                                    tint = Orange,
                                    modifier = Modifier.size(34.dp),
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                AutoText(
                                    text = booking.testName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = theme.textPrimary,
                                )
                                Spacer(Modifier.height(8.dp))
                                Surface(
                                    color = theme.orangeChipBg,
                                    shape = RoundedCornerShape(20.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = Orange,
                                            modifier = Modifier.size(14.dp),
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        AutoText(
                                            text = booking.status,
                                            color = Orange,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = theme.cardBg),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(modifier = Modifier.padding(4.dp)) {
                        DetailRow(
                            icon = Icons.Default.Badge,
                            label = "Booking ID",
                            value = booking.id,
                            theme = theme,
                        )
                        RowDivider(theme = theme)

                        DetailRow(
                            icon = Icons.AutoMirrored.Filled.Assignment,
                            label = "Test Name",
                            value = booking.testName,
                            theme = theme,
                        )
                        RowDivider(theme = theme)

                        DetailRow(
                            icon = Icons.Default.AccessTime,
                            label = "Status",
                            value = "",
                            theme = theme,
                            trailingContent = {
                                Surface(
                                    color = theme.orangeChipBg,
                                    shape = RoundedCornerShape(20.dp),
                                ) {
                                    AutoText(
                                        text = booking.status,
                                        color = Orange,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    )
                                }
                            }
                        )
                        RowDivider(theme = theme)

                        DetailRow(
                            icon = Icons.Default.CurrencyRupee,
                            label = "Amount",
                            value = "₹ ${booking.amount}",
                            theme = theme,
                        )
                        RowDivider(theme = theme)

                        DetailRow(
                            icon = Icons.Default.CalendarMonth,
                            label = "Collection Date",
                            value = booking.collectionDate,
                            theme = theme,
                            trailingContent = {
                                Surface(
                                    color = theme.greenChipBg,
                                    shape = RoundedCornerShape(12.dp),
                                ) {
                                    AutoText(
                                        text = getDaysRemaining(booking.collectionDate),
                                        color = GreenText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    )
                                }
                            }
                        )
                        RowDivider(theme = theme)

                        DetailRow(
                            icon = Icons.Default.Person,
                            label = "Patient Name",
                            value = historyViewModel.getPatientName(),
                            theme = theme,
                        )
                        RowDivider(theme = theme)

                        DetailRow(
                            icon = Icons.Default.Phone,
                            label = "Phone",
                            value = historyViewModel.getPatientPhone(),
                            theme = theme,
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }

        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = { AutoText("Cancel Booking") },
                text = { AutoText("Are you sure you want to cancel this booking?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showCancelDialog = false
                            historyViewModel.cancelBooking(booking.id)
                        }
                    ) {
                        AutoText("Yes, Cancel")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCancelDialog = false }) {
                        AutoText("No")
                    }
                }
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    theme: BookingTheme,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(theme.purpleChipBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Purple,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            AutoText(
                text = label,
                fontSize = 12.sp,
                color = theme.textMuted,
            )
            if (value.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                AutoText(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.textPrimary,
                )
            }
            if (trailingContent != null && value.isEmpty()) {
                Spacer(Modifier.height(4.dp))
                trailingContent()
            }
        }
        if (trailingContent != null && value.isNotEmpty()) {
            trailingContent()
        }
    }
}

@Composable
private fun RowDivider(theme: BookingTheme) {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = theme.divider,
        thickness = 1.dp,
    )
}