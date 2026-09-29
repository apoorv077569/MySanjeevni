package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components.DoctorCard
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components.EmergencyWarningDialog
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components.FilterDialog
import com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel.ConsultViewModel

private val ConsultGreen = Color(0xFF00A878)
private val ScreenBackground = Color(0xFFF2FFFB)
private val TextDark = Color(0xFF071B35)
private val TextGrey = Color(0xFF60738A)

@Composable
fun ConsultScreen(
    navController: NavController,
    viewModel: ConsultViewModel = hiltViewModel(),
    onViewSlotsClick: (Doctor) -> Unit = {},
    onBookNowClick: (Doctor) -> Unit = {}
) {

    val doctorState by viewModel.doctorState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val doctors by viewModel.searchResults.collectAsState()
    val departments by viewModel.departments.collectAsState()
    val specializations by viewModel.specializations.collectAsState()

    val selectedDepartment by viewModel.selectedDepartment.collectAsState()
    val selectedSpecialization by viewModel.selectedSpecialization.collectAsState()

    var showFilterDialog by remember { mutableStateOf(false) }
    var showEmergencyDialog by remember { mutableStateOf(false) }
    var selectedDoctorForBooking by remember { mutableStateOf<Doctor?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Find a Doctor",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "${doctors.size} doctors available",
                        fontSize = 16.sp,
                        color = TextGrey
                    )
                }

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {

                    IconButton(
                        onClick = {
                            showFilterDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = "Filter",
                            tint = ConsultGreen
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Search doctor or specialty"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            when {

                // Loading
                doctorState.isLoading -> {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color = ConsultGreen
                        )
                    }
                }

                // Error
                doctorState.error != null -> {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = doctorState.error
                                    ?: "Something went wrong",
                                color = MaterialTheme.colorScheme.error
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Button(
                                onClick = viewModel::retry,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ConsultGreen
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                // Empty
                doctors.isEmpty() -> {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = if (searchQuery.isBlank()) {
                                "No doctors available"
                            } else {
                                "No doctors found"
                            },
                            color = TextGrey
                        )
                    }
                }

                // Doctors
                else -> {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),

                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(
                            items = doctors,
                            key = { it.id }
                        ) { doctor ->

                            DoctorCard(
                                doctor = doctor,

                                onViewSlotsClick = {
                                    onViewSlotsClick(doctor)
                                },

                                onBookNowClick = {
                                    selectedDoctorForBooking = doctor
                                    showEmergencyDialog = true
                                }
                            )
                        }

                        item {
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    if (showFilterDialog) {
        FilterDialog(
            departments = departments,
            specializations = specializations,
            selectedDepartment = selectedDepartment,
            selectedSpecialization = selectedSpecialization,

            onDepartmentSelected = {
                viewModel.setDepartment(it)
            },

            onSpecializationSelected = {
                viewModel.setSpecialization(it)
            },

            onClear = {
                viewModel.clearFilters()
            },

            onDismiss = {
                showFilterDialog = false
            }
        )
    }
    if (showEmergencyDialog) {
        EmergencyWarningDialog(
            onDismiss = {
                showEmergencyDialog = false
                selectedDoctorForBooking = null
            },
            onConfirm = {
                showEmergencyDialog = false
                selectedDoctorForBooking?.let { doctor ->
                    navController.navigate(Screen.BookConsultation.createRoute(doctor.id))
                }
                selectedDoctorForBooking = null
            }
        )
    }
}