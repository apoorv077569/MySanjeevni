package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
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
    val isDarkTheme = isSystemInDarkTheme()

    val backgroundColor = if (isDarkTheme) { Color(0xFF101A17) } else { Color(0xFFF2FFFB) }
    val topBarColor = if (isDarkTheme) { Color(0xFF162A25) } else { Color(0xFFE9FAF6) }
    val topCircleColor = if (isDarkTheme) { Color(0xFF1D3932) } else { Color(0xFFD4F5EA) }
    val topCircleColor2 = if (isDarkTheme) { Color(0xFF23483E) } else { Color(0xFFC8F0E2) }
    val textColor = if (isDarkTheme) { Color(0xFFF1F5F3) } else { Color(0xFF071B35) }
    val greyColor = if (isDarkTheme) { Color(0xFFAABBB5) } else { Color(0xFF60738A) }
    val surfaceColor = if (isDarkTheme) { Color(0xFF1A2421) } else { Color.White }
    val rxLineColor = if (isDarkTheme) { Color(0xFF526862) } else { Color(0xFFD9E4EA) }
    val prescriptionGreen = Color(0xFF00A878)

    LaunchedEffect(consultationId) {
        viewModel.loadPrescription(
            consultationId = consultationId
        )
    }

    Scaffold(
        containerColor = backgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // -------------------------------------------------
            // Top Bar
            // -------------------------------------------------

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 32.dp,
                            bottomEnd = 32.dp
                        )
                    )
                    .background(
                        color = topBarColor
                    )
                    .background(
                        color = topCircleColor,
                        shape = CircleShape
                    )
                    .background(
                        color = topCircleColor2,
                        shape = CircleShape
                    )
            ) {

                // Decorative mint circles
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .offset(
                            x = 250.dp,
                            y = (-45).dp
                        )
                        .background(
                            color = Color(0xFFD4F5EA),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .offset(
                            x = 330.dp,
                            y = 35.dp
                        )
                        .background(
                            color = Color(0xFFC8F0E2),
                            shape = CircleShape
                        )
                )

                // Rx document decoration
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(
                            x = (-8).dp,
                            y = 8.dp
                        )
                        .rotate(7f)
                        .size(
                            width = 70.dp,
                            height = 58.dp
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = surfaceColor
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        AutoText(
                            text = "Rx",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00A878)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFD9E4EA))
                            )
                        }
                    }
                }

                // Main top bar content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            top = 16.dp,
                            end = 20.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Circular back button
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                clip = false
                            )
                            .clip(CircleShape)
                            .background(surfaceColor)
                            .clickable {
                                navController.popBackStack()
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(24.dp)
                    )

                    AutoText(
                        text = "Prescription",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
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
                            color = greyColor,
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