package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.BookConsultationUiState
import com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel.BookConsultationViewModel

// Design Colors
private val PrimaryGreen = Color(0xFF00A878)
private val LightGreen = Color(0xFFF2FFFB)
private val Yellowish = Color(0xFFFFF9E6)
private val HeaderGradientStart = Color(0xFF00A878)
private val HeaderGradientEnd = Color(0xFF00796B)

private fun getPlainErrorMessage(error: String): String {
    return try {
        val json = org.json.JSONObject(error)

        json.optString("message")
            .takeIf { it.isNotBlank() }
            ?: json.optString("error")
                .takeIf { it.isNotBlank() }
            ?: error
    } catch (e: Exception) {
        error
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookConsultationScreen(
    doctorId: String,
    onNavigateBack: () -> Unit,
    rescheduleId: String? = null,
    viewModel: BookConsultationViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPatientFromSession()
    }
    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }
    LaunchedEffect(uiState.isBookingSuccessful) {

        if (uiState.isBookingSuccessful) {

            Toast.makeText(
                context,
                "Consultation booked successfully",
                Toast.LENGTH_SHORT
            ).show()

            viewModel.resetBookingState()

            onNavigateBack()
        }
    }

    LaunchedEffect(uiState.error) {
        if (!uiState.error.isNullOrBlank()) {
            showErrorDialog = true
        }
    }


    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
            },
            title = {
                Text(text = "Error")
            },
            text = {
                Text(text = getPlainErrorMessage(uiState.error.orEmpty()),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showErrorDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            BookingFooter(
                onCancel = onNavigateBack,
                onBook = { viewModel.onBookAppointment() },
                isLoading = uiState.isLoading
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(onClose = onNavigateBack)

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (maxWidth > 800.dp) {
                    // Two-column layout for larger screens
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            SummarySection(uiState)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            FormSection(uiState, viewModel)
                        }
                    }
                } else {
                    // Single column layout for mobile
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SummarySection(uiState)
                        FormSection(uiState, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(HeaderGradientStart, HeaderGradientEnd)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Complete Your Booking",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fill in your details to lock your appointment slot",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}

@Composable
fun SummarySection(uiState: BookConsultationUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Doctor Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = PrimaryGreen.copy(alpha = 0.1f),
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Dr", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(uiState.doctor?.name ?: "Loading...", fontWeight = FontWeight.Bold)
                    Text(uiState.doctor?.department ?: "", color = Color.Gray, fontSize = 14.sp)
                    Text("${uiState.doctor?.experience?.toInt() ?: 15} years experience", color = Color.Gray, fontSize = 12.sp)
                    Text("Fee: ${if (uiState.doctor?.consultationFee == 0.0) "Free" else "₹${uiState.doctor?.consultationFee}"}", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
        }

        // View Slots Box
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = LightGreen,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "No specific appointment dates set by doctor yet",
                modifier = Modifier.padding(16.dp),
                color = PrimaryGreen,
                fontSize = 14.sp
            )
        }

        // Info Box
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Yellowish,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Doctor confirms exact time after booking. Queue token is generated instantly.",
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF856404),
                fontSize = 14.sp
            )
        }

        // Booking Steps
        Column {
            Text("Booking Steps:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Text("1. Enter patient details", fontSize = 14.sp, color = Color.Gray)
            Text("2. Choose date and preferred slot", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSection(
    uiState: BookConsultationUiState,
    viewModel: BookConsultationViewModel
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = uiState.patientName,
            onValueChange = { viewModel.onPatientNameChange(it) },
            label = { Text("Patient Name *") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.error != null && uiState.patientName.isBlank()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = { Text("Phone") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = uiState.appointmentDate
                .takeIf { it.isNotBlank() }
                ?.let {
                    try {
                        val inputFormatter =
                            java.text.SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss",
                                java.util.Locale.US
                            )

                        inputFormatter.timeZone =
                            java.util.TimeZone.getTimeZone("UTC")

                        val outputFormatter =
                            java.text.SimpleDateFormat(
                                "dd MMM yyyy",
                                java.util.Locale.US
                            )

                        outputFormatter.format(
                            inputFormatter.parse(it)!!
                        )

                    } catch (e: Exception) {
                        it
                    }
                }
                ?: "",
            onValueChange = {},
            label = {
                Text("Appointment Date *")
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker = true
                },
            trailingIcon = {
                IconButton(
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date"
                    )
                }
            },
            readOnly = true,
            isError = uiState.error != null &&
                    uiState.appointmentDate.isBlank()
        )
        if (showDatePicker) {

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            datePickerState.selectedDateMillis?.let { millis ->

                                val formatter = java.text.SimpleDateFormat(
                                    "yyyy-MM-dd'T'HH:mm:ss",
                                    java.util.Locale.US
                                )

                                formatter.timeZone =
                                    java.util.TimeZone.getTimeZone("UTC")

                                val selectedDate = formatter.format(
                                    java.util.Date(millis)
                                )

                                viewModel.onDateChange(
                                    selectedDate
                                )
                            }

                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {

                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {

                DatePicker(
                    state = datePickerState
                )
            }
        }

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = uiState.consultationType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Consultation Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("In-Person Visit") },
                    onClick = {
                        viewModel.onConsultationTypeChange("in-person")
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Video Consultation") },
                    onClick = {
                        viewModel.onConsultationTypeChange("video")
                        expanded = false
                    }
                )
            }
        }

        OutlinedTextField(
            value = uiState.symptoms,
            onValueChange = { viewModel.onSymptomChange(it) },
            label = { Text("Symptoms / Reason") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            maxLines = 5
        )
    }
}

@Composable
fun BookingFooter(onCancel: () -> Unit, onBook: () -> Unit, isLoading: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onCancel, enabled = !isLoading) {
                Text("Cancel")
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = onBook,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Book")
                }
            }
        }
    }
}