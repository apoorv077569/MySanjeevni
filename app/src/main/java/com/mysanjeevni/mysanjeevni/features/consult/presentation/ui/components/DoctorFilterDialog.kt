package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ConsultGreen = Color(0xFF00A878)

@Composable
fun FilterDialog(
    departments: List<String>,
    specializations: List<String>,

    selectedDepartment: String?,
    selectedSpecialization: String?,

    onDepartmentSelected: (String?) -> Unit,
    onSpecializationSelected: (String?) -> Unit,

    onClear: () -> Unit,
    onDismiss: () -> Unit
) {

    var departmentExpanded by remember {
        mutableStateOf(false)
    }

    var specializationExpanded by remember {
        mutableStateOf(false)
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text(
                text = "Filter Doctors"
            )
        },

        text = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Department
                Text(
                    text = "Department"
                )

                androidx.compose.material3.OutlinedButton(
                    onClick = {
                        departmentExpanded = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = selectedDepartment
                            ?: "Select Department"
                    )
                }

                DropdownMenu(
                    expanded = departmentExpanded,
                    onDismissRequest = {
                        departmentExpanded = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("All Departments")
                        },
                        onClick = {
                            onDepartmentSelected(null)
                            departmentExpanded = false
                        }
                    )

                    departments.forEach { department ->

                        DropdownMenuItem(
                            text = {
                                Text(department)
                            },
                            onClick = {
                                onDepartmentSelected(department)
                                departmentExpanded = false
                            }
                        )
                    }
                }

                // Specialization
                Text(
                    text = "Specialization",
                    modifier = Modifier.padding(top = 20.dp)
                )

                androidx.compose.material3.OutlinedButton(
                    onClick = {
                        specializationExpanded = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = selectedSpecialization
                            ?: "Select Specialization"
                    )
                }

                DropdownMenu(
                    expanded = specializationExpanded,
                    onDismissRequest = {
                        specializationExpanded = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("All Specializations")
                        },
                        onClick = {
                            onSpecializationSelected(null)
                            specializationExpanded = false
                        }
                    )

                    specializations.forEach { specialization ->

                        DropdownMenuItem(
                            text = {
                                Text(specialization)
                            },
                            onClick = {
                                onSpecializationSelected(
                                    specialization
                                )
                                specializationExpanded = false
                            }
                        )
                    }
                }
            }
        },

        confirmButton = {

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ConsultGreen
                )
            ) {
                Text("Apply")
            }
        },

        dismissButton = {

            TextButton(
                onClick = {
                    onClear()
                }
            ) {
                Text("Clear")
            }
        }
    )
}