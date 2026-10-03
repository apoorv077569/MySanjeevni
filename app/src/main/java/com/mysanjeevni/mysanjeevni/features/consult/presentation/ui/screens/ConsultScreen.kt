package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.screens

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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.clip
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
    val isDarkTheme = isSystemInDarkTheme()
    val screenBackground = if (isDarkTheme) Color(0xFF101A17) else Color(0xFFF2FFFB)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F3) else Color(0xFF071B35)
    val greyColor = if (isDarkTheme) Color(0xFFAABBB5) else Color(0xFF60738A)
    val surfaceColor = if (isDarkTheme) Color(0xFF1A2421) else Color.White

    Box(
        modifier = Modifier.fillMaxSize().background(screenBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 28.dp,
                            bottomEnd = 28.dp
                        )
                    )
                    .background(if (isDarkTheme) { Color(0xFF162A25) } else { Color(0xFFE9FAF6)})
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .offset(x = 285.dp, y = (-35).dp)
                        .background(color = if (isDarkTheme) { Color(0xFF1D3932) } else { Color(0xFFD4F5EA) }, shape = CircleShape)
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .offset(
                            x = 325.dp,
                            y = 60.dp
                        )
                        .background(
                            color = if (isDarkTheme) {
                                Color(0xFF23483E)
                            } else {
                                Color(0xFFC8F0E2)
                            },
                            shape = CircleShape
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 18.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Find a Doctor",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "${doctors.size} doctors available",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = greyColor
                        )
                    }

                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = if (isDarkTheme) {
                            Color(0xFF23483E)
                        } else {
                            Color.White
                        },
                        shadowElevation = 3.dp
                    ) {

                        IconButton(
                            onClick = {
                                showFilterDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = "Filter",
                                tint = if (isDarkTheme) {
                                    Color(0xFF35C99A)
                                } else {
                                    ConsultGreen
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }
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
                        text = "Search doctor or specialty",
                        color = greyColor
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = greyColor
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
                            color = greyColor
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