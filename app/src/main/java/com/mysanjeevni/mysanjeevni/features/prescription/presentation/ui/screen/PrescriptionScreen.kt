package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.components.PrescriptionContent
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.utils.downloadPrescriptionPdf
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.viewmodel.PrescriptionViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

val PrescriptionGreen = Color(0xFF00A878)
val BackgroundColor = Color(0xFFF2FFFB)
val TextDark = Color(0xFF071B35)
val TextGrey = Color(0xFF60738A)

@Composable
fun PrescriptionScreen(
    navController: NavController,
    prescriptionId: String? = null,
    consultationId: String? = null,
    viewModel: PrescriptionViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(consultationId) {
        viewModel.loadPrescription(
            consultationId = consultationId
        )
    }

    Scaffold(
        containerColor = BackgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // -------------------------------------------------
            // Top Bar
            // -------------------------------------------------

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark
                    )
                }

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                AutoText(
                    text = "Prescription",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            when {

                state.isLoading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color = PrescriptionGreen
                        )
                    }
                }

                state.error != null -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            AutoText(
                                text = state.error
                                    ?: "Something went wrong",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 15.sp
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Button(
                                onClick = {
                                    viewModel.retry()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrescriptionGreen
                                )
                            ) {

                                AutoText(
                                    text = "Retry",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                state.prescriptions.isEmpty() -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        AutoText(
                            text = "No prescription found",
                            color = TextGrey,
                            fontSize = 15.sp
                        )
                    }
                }

                else -> {
                    if (prescriptionId != null) {
                        val prescription = state.prescriptions.firstOrNull {
                            it.id == prescriptionId
                        }
                        if (prescription != null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                PrescriptionContent(prescription = prescription)
                            }
                            Button(
                                onClick = {
                                    val downloaded = downloadPrescriptionPdf(
                                        context, prescription = prescription
                                    )
                                    Toast.makeText(
                                        context,
                                        if (downloaded) {
                                            "Prescription downloaded successfully"
                                        } else {
                                            "Failed to download prescription"
                                        },
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 10.dp
                                    )
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrescriptionGreen
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download PDF",
                                    tint = Color.White
                                )

                                Spacer(
                                    modifier = Modifier.width(8.dp)
                                )

                                AutoText(
                                    text = "Download PDF",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = state.prescriptions,
                                key = { it.id }
                            ) { prescription ->
                                PrescriptionContent(
                                    prescription = prescription,
                                    onDownload = {
                                        val downloaded = downloadPrescriptionPdf(context = context,prescription = prescription)
                                        Toast.makeText(context,if (downloaded){"Prescription Downloaded Successfully"}else{"Failed to download prescription"},
                                            Toast.LENGTH_SHORT).show()

                                    }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}