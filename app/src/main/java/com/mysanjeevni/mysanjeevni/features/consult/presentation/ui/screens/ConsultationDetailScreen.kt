package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components.formatAppointmentDate
import com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel.ConsultationDetailViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationDetailScreen(
    consultation: Consultation,
    onNavigateBack: () -> Unit,
    onVideoCallClick: (String) -> Unit,
    viewModel: ConsultationDetailViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var showCancelDialog by remember {
        mutableStateOf(false)
    }

    val colorScheme = MaterialTheme.colorScheme

    // ---------------------------------------------------------
    // Load consultation
    // ---------------------------------------------------------

    LaunchedEffect(consultation.id) {

        viewModel.setConsultation(
            consultation
        )
    }

    // ---------------------------------------------------------
    // Cancellation successful
    // ---------------------------------------------------------

    LaunchedEffect(state.isCancelled) {

        if (state.isCancelled) {
            onNavigateBack()
        }
    }

    // ---------------------------------------------------------
    // SCREEN
    // ---------------------------------------------------------

    Scaffold(

        containerColor = colorScheme.background,

        topBar = {

            TopAppBar(

                title = {

                    AutoText(
                        text = "Consultation Details",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onNavigateBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription = "Back",

                            tint = colorScheme.onSurface
                        )
                    }
                },

                actions = {

                    if (
                        consultation.consultationType.equals(
                            "video",
                            ignoreCase = true
                        )
                    ) {

                        IconButton(
                            onClick = {

                                val channelName =
                                    "consult_${consultation.id}"

                                onVideoCallClick(
                                    channelName
                                )
                            }
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.VideoCall,

                                contentDescription =
                                    "Join Video Call",

                                tint =
                                    colorScheme.primary,

                                modifier =
                                    Modifier.size(28.dp)
                            )
                        }
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            colorScheme.background,

                        titleContentColor =
                            colorScheme.onBackground,

                        navigationIconContentColor =
                            colorScheme.onBackground,

                        actionIconContentColor =
                            colorScheme.primary
                    )
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    colorScheme.background
                )
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            // =================================================
            // SINGLE MAIN CARD
            // =================================================

            DetailCard {

                // =================================================
                // DOCTOR
                // =================================================

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Surface(

                        modifier =
                            Modifier.size(56.dp),

                        shape =
                            RoundedCornerShape(28.dp),

                        color =
                            colorScheme.primaryContainer
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                null,

                            tint =
                                colorScheme.primary,

                            modifier =
                                Modifier.padding(14.dp)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(14.dp)
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        AutoText(

                            text =
                                consultation.doctorName,

                            fontSize = 19.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                colorScheme.onSurface
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        AutoText(

                            text =
                                consultation.specialization,

                            fontSize = 14.sp,

                            fontWeight =
                                FontWeight.Medium,

                            color =
                                colorScheme.primary
                        )

                        Spacer(
                            modifier =
                                Modifier.height(2.dp)
                        )

                        AutoText(

                            text =
                                consultation.department,

                            fontSize = 12.sp,

                            color =
                                colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(22.dp)
                )

                // =================================================
                // APPOINTMENT DETAILS
                // =================================================

                SectionTitle(
                    text = "Appointment Details"
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                DetailRow(

                    icon =
                        Icons.Default.CalendarToday,

                    label = "Date",

                    value =
                        formatAppointmentDate(
                            consultation.appointmentDate
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(13.dp)
                )

                DetailRow(

                    icon =
                        Icons.Default.Schedule,

                    label = "Time",

                    value =
                        consultation.allottedTime
                            .ifBlank {
                                "Not assigned"
                            }
                )

                Spacer(
                    modifier =
                        Modifier.height(13.dp)
                )

                DetailRow(

                    icon =
                        if (
                            consultation.consultationType
                                .equals(
                                    "video",
                                    ignoreCase = true
                                )
                        ) {

                            Icons.Default.VideoCall

                        } else {

                            Icons.Default.Schedule
                        },

                    label =
                        "Consultation Type",

                    value =
                        consultation.consultationType
                            .replace(
                                "-",
                                " "
                            )
                            .replaceFirstChar {
                                it.uppercase()
                            }
                )

                Spacer(
                    modifier =
                        Modifier.height(13.dp)
                )

                DetailRow(

                    icon =
                        Icons.Default.Person,

                    label =
                        "Queue Number",

                    value =
                        consultation.queueNumber
                            .toString()
                )

                Spacer(
                    modifier =
                        Modifier.height(13.dp)
                )

                DetailRow(

                    icon =
                        Icons.Default.Person,

                    label =
                        "Patients Ahead",

                    value =
                        consultation.patientsAhead
                            .toString()
                )

                Spacer(
                    modifier =
                        Modifier.height(22.dp)
                )

                // =================================================
                // PATIENT INFORMATION
                // =================================================

                SectionTitle(
                    text = "Patient Information"
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                DetailRow(

                    icon =
                        Icons.Default.Person,

                    label =
                        "Patient Name",

                    value =
                        consultation.patientName
                            .ifBlank {
                                "Not available"
                            }
                )

                if (
                    consultation.patientPhone
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(13.dp)
                    )

                    DetailRow(

                        icon =
                            Icons.Default.Call,

                        label =
                            "Phone",

                        value =
                            consultation.patientPhone
                    )
                }

                if (
                    consultation.patientEmail
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(13.dp)
                    )

                    DetailRow(

                        icon =
                            Icons.Default.Email,

                        label =
                            "Email",

                        value =
                            consultation.patientEmail
                    )
                }

                // =================================================
                // SYMPTOMS
                // =================================================

                if (
                    consultation.symptoms
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(22.dp)
                    )

                    SectionTitle(
                        text = "Symptoms"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Surface(

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(10.dp),

                        color =
                            colorScheme.surfaceVariant
                    ) {

                        AutoText(

                            text =
                                consultation.symptoms,

                            fontSize = 14.sp,

                            color =
                                colorScheme.onSurfaceVariant,

                            modifier =
                                Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(22.dp)
                )

                // =================================================
                // PAYMENT
                // =================================================

                SectionTitle(
                    text = "Payment"
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        AutoText(

                            text =
                                "Consultation Fee",

                            fontSize = 12.sp,

                            color =
                                colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        AutoText(

                            text =
                                if (
                                    consultation.fees <= 0
                                ) {

                                    "Free"

                                } else {

                                    "₹${consultation.fees}"
                                },

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                colorScheme.onSurface
                        )
                    }

                    val isRefunded =
                        consultation.paymentStatus
                            .equals(
                                "refunded",
                                ignoreCase = true
                            )

                    Surface(

                        shape =
                            RoundedCornerShape(20.dp),

                        color =
                            if (isRefunded) {

                                colorScheme.errorContainer

                            } else {

                                colorScheme.tertiaryContainer
                            }
                    ) {

                        AutoText(

                            text =
                                consultation.paymentStatus
                                    .replaceFirstChar {
                                        it.uppercase()
                                    },

                            fontSize = 12.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                if (isRefunded) {

                                    colorScheme.onErrorContainer

                                } else {

                                    colorScheme.onTertiaryContainer
                                },

                            modifier =
                                Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 7.dp
                                )
                        )
                    }
                }

                // =================================================
                // NOTES
                // =================================================

                if (
                    consultation.notes
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(22.dp)
                    )

                    SectionTitle(
                        text = "Notes"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Surface(

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(10.dp),

                        color =
                            colorScheme.surfaceVariant
                    ) {

                        AutoText(

                            text =
                                consultation.notes,

                            fontSize = 14.sp,

                            color =
                                colorScheme.onSurfaceVariant,

                            modifier =
                                Modifier.padding(12.dp)
                        )
                    }
                }

                // =================================================
                // CANCELLED MESSAGE
                // =================================================

                if (
                    consultation.status.equals(
                        "cancelled",
                        ignoreCase = true
                    )
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(20.dp)
                    )

                    Surface(

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(12.dp),

                        color =
                            colorScheme.errorContainer
                    ) {

                        AutoText(

                            text =
                                "This consultation has been cancelled.",

                            fontSize = 13.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            color =
                                colorScheme.onErrorContainer,

                            modifier =
                                Modifier.padding(16.dp)
                        )
                    }
                }

                // =================================================
                // ERROR
                // =================================================

                state.error?.let { error ->

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Surface(

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(10.dp),

                        color =
                            colorScheme.errorContainer
                    ) {

                        AutoText(

                            text =
                                error,

                            fontSize = 13.sp,

                            color =
                                colorScheme.onErrorContainer,

                            modifier =
                                Modifier.padding(14.dp)
                        )
                    }
                }
            }

            // =================================================
            // CANCEL BUTTON
            // =================================================

            if (
                !consultation.status.equals(
                    "completed",
                    ignoreCase = true
                ) &&
                !consultation.status.equals(
                    "cancelled",
                    ignoreCase = true
                )
            ) {

                Button(

                    onClick = {
                        showCancelDialog = true
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                    shape =
                        RoundedCornerShape(12.dp),

                    enabled =
                        !state.isCancelling,

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                colorScheme.error,

                            contentColor =
                                colorScheme.onError
                        )
                ) {

                    if (state.isCancelling) {

                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(22.dp),

                            color =
                                colorScheme.onError,

                            strokeWidth = 2.dp
                        )

                    } else {

                        Icon(

                            imageVector =
                                Icons.Default.Cancel,

                            contentDescription =
                                null
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        AutoText(

                            text =
                                "Cancel Consultation",

                            fontSize = 14.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                colorScheme.onError
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
        }
    }

    // =========================================================
    // CANCEL CONFIRMATION DIALOG
    // =========================================================

    if (showCancelDialog) {

        AlertDialog(

            onDismissRequest = {
                showCancelDialog = false
            },

            title = {

                AutoText(

                    text =
                        "Cancel Consultation?",

                    fontSize = 18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        colorScheme.onSurface
                )
            },

            text = {

                AutoText(

                    text =
                        "Are you sure you want to cancel this consultation? If payment was made, the refund will be initiated to the original payment account.",

                    fontSize = 14.sp,

                    color =
                        colorScheme.onSurfaceVariant
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        showCancelDialog = false

                        viewModel.cancelConsultation()
                    },

                    shape =
                        RoundedCornerShape(8.dp),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                colorScheme.error,

                            contentColor =
                                colorScheme.onError
                        )
                ) {

                    AutoText(

                        text =
                            "Yes, Cancel",

                        color =
                            colorScheme.onError,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        showCancelDialog = false
                    }
                ) {

                    AutoText(

                        text =
                            "Keep Consultation",

                        color =
                            colorScheme.primary
                    )
                }
            }
        )
    }
}


// =============================================================
// SINGLE DETAIL CARD
// =============================================================

@Composable
private fun DetailCard(
    content: @Composable () -> Unit
) {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        color =
            MaterialTheme.colorScheme.surface,

        tonalElevation =
            2.dp
    ) {

        Column(

            modifier =
                Modifier.padding(18.dp)
        ) {

            content()
        }
    }
}


// =============================================================
// SECTION TITLE
// =============================================================

@Composable
private fun SectionTitle(
    text: String
) {

    AutoText(

        text = text,

        fontSize = 16.sp,

        fontWeight =
            FontWeight.Bold,

        color =
            MaterialTheme.colorScheme.onSurface
    )
}


// =============================================================
// DETAIL ROW
// =============================================================

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {

    val colorScheme =
        MaterialTheme.colorScheme

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Surface(

            modifier =
                Modifier.size(34.dp),

            shape =
                RoundedCornerShape(9.dp),

            color =
                colorScheme.primaryContainer
        ) {

            Icon(

                imageVector =
                    icon,

                contentDescription =
                    null,

                tint =
                    colorScheme.primary,

                modifier =
                    Modifier.padding(8.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.width(12.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            AutoText(

                text =
                    label,

                fontSize = 11.sp,

                color =
                    colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            AutoText(

                text =
                    value.ifBlank {
                        "Not available"
                    },

                fontSize = 14.sp,

                fontWeight =
                    FontWeight.Medium,

                color =
                    colorScheme.onSurface
            )
        }
    }
}